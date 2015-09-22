package com.palomamobile.android.sdk.message;

import android.test.InstrumentationTestCase;
import android.util.Log;
import com.palomamobile.android.sdk.auth.PasswordUserCredential;
import com.palomamobile.android.sdk.core.PaginatedResponse;
import com.palomamobile.android.sdk.core.ServiceRequestParams;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.palomamobile.android.sdk.user.User;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class MessageManagerInstrumentationTest extends InstrumentationTestCase {

    public static final String TAG = MessageManagerInstrumentationTest.class.getSimpleName();

    private IMessageManager messageManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        messageManager = ServiceSupport.Instance.getServiceManager(IMessageManager.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        ServiceSupport.Instance.getEventBus().unregister(this);
    }

    public void testRequestRefreshMessagesReceived() throws Throwable {
        TestUtilities.registerUserSynchronous(this);

        final LatchedBusListener<EventMessagesReceived> latchedBusListener = new LatchedBusListener<>(EventMessagesReceived.class);

        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobGetMessagesReceived jobRefreshMessagesReceived = messageManager.createJobGetMessagesReceived();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRefreshMessagesReceived);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        assertNotNull(latchedBusListener.getEvent());
        assertNull(latchedBusListener.getEvent().getFailure());
        assertNull(latchedBusListener.getEvent().getSuccess().getEmbedded());
    }


    public void testFiterMessagesReceived() throws Throwable {
        //create a "friend 1"
        final String tmp1 = String.valueOf(System.currentTimeMillis());
        User friend1 = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp1, tmp1));
        assertNotNull(friend1);
        Log.i(TAG, "friend1 created!!");
        final long friendId1 = friend1.getId();

        //create a "friend 2"
        final String tmp2 = String.valueOf(System.currentTimeMillis());
        User friend2 = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp2, tmp2));
        assertNotNull(friend2);
        Log.i(TAG, "friend2 created!!");
        final long friendId2 = friend2.getId();
        assertFalse(friendId1 == friendId2);

        //create a local user
        final String tmpSelf = String.valueOf(System.currentTimeMillis());
        User self = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmpSelf, tmpSelf));
        Log.i(TAG, "self created!!");
        final long selfId = self.getId();
        assertNotNull(self);
        assertFalse(friendId1 == selfId);
        assertFalse(friendId2 == selfId);

        //login as friend1 and send some messages to self
        TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp1, tmp1));
        messageManager.createJobPostMessageToFriend("text/plain", "test1", "https://bit.ly/1", selfId).syncRun(false);
        messageManager.createJobPostMessageToFriend("text/plain", "test2", "https://bit.ly/2", selfId).syncRun(false);

        //login as friend2 and send some messages to self
        TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp2, tmp2));
        messageManager.createJobPostMessageToFriend("text/plain", "test1", "https://other.com/1", selfId).syncRun(false);
        messageManager.createJobPostMessageToFriend("text/plain", "test2", "https://other.com/2", selfId).syncRun(false);

        //login as self and check messages
        self = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmpSelf, tmpSelf));

        PaginatedResponse<MessageReceived> messageReceivedPaginatedResponse = messageManager.createJobGetMessagesReceived().setServiceRequestParams(
                new ServiceRequestParams()
                        .setPageIndex(0)
                        .setResultsPerPage(30)
                        .setFilterQuery("sender.userId='"+friendId1+"'")
                        .sort("id", ServiceRequestParams.Sort.Order.Desc)
        ).syncRun(false);
        assertEquals(2, messageReceivedPaginatedResponse.getEmbedded().getItems().size());
        assertEquals("https://bit.ly/2", messageReceivedPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0).getUrl());
        assertEquals("test2", messageReceivedPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0).getPayload());
        assertEquals("https://bit.ly/1", messageReceivedPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0).getUrl());
        assertEquals("test1", messageReceivedPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0).getPayload());


        messageReceivedPaginatedResponse = messageManager.createJobGetMessagesReceived().setServiceRequestParams(
                new ServiceRequestParams()
                        .setPageIndex(0)
                        .setResultsPerPage(30)
                        .setFilterQuery("sender.userId='"+friendId2+"'")
                        .sort("id", ServiceRequestParams.Sort.Order.Desc)
        ).syncRun(false);
        assertEquals(2, messageReceivedPaginatedResponse.getEmbedded().getItems().size());
        assertEquals("https://other.com/2", messageReceivedPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0).getUrl());
        assertEquals("https://other.com/1", messageReceivedPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0).getUrl());

        messageReceivedPaginatedResponse = messageManager.createJobGetMessagesReceived().setServiceRequestParams(
                new ServiceRequestParams()
                        .setPageIndex(0)
                        .setResultsPerPage(30)
                        .setFilterQuery("sender.userId='"+friendId2+"'|sender.userId='"+friendId1+"'")
                        .sort("id", ServiceRequestParams.Sort.Order.Desc)
        ).syncRun(false);
        assertEquals(4, messageReceivedPaginatedResponse.getEmbedded().getItems().size());
        assertEquals("https://other.com/2", messageReceivedPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0).getUrl());
        assertEquals("https://other.com/1", messageReceivedPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0).getUrl());
        assertEquals("https://bit.ly/2", messageReceivedPaginatedResponse.getEmbedded().getItems().get(2).getContentList().get(0).getUrl());
        assertEquals("https://bit.ly/1", messageReceivedPaginatedResponse.getEmbedded().getItems().get(3).getContentList().get(0).getUrl());
    }

    public void testPaginatedMessagesSentReceivedDelete() throws Throwable {
        //create a "friend"
        String tmp = String.valueOf(System.currentTimeMillis());
        User friend = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp, tmp));
        assertNotNull(friend);
        Log.i(TAG, "friend created!!");
        final long friendId = friend.getId();

        //create a local user
        User self = TestUtilities.registerUserSynchronous(this);
        Log.i(TAG, "self created!!");
        assertNotNull(self);
        assertTrue(friendId != self.getId());

        final String contentType = "text/html";

        //https://duckduckgo.com/?q=1&ia=about
        int counter = 0;
        messageManager.createJobPostMessageToFriend(contentType, null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend(contentType, null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend(contentType, null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);

        messageManager.createJobPostMessageToFriend(contentType, null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("text/css", null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("application/octet-stream", null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);

        messageManager.createJobPostMessageToFriend("image/gif", null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("image/svg+xml", null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("text/plain", null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);

        messageManager.createJobPostMessageToFriend(contentType, null, "https://duckduckgo.com/?q=" + counter + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("image/png", null, "https://www.google.com/images/srpr/logo11w.png", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("image/png", null, "http://www.workjoke.com/images/logo.png", friendId).syncRun(false);

        PaginatedResponse<MessageSent> messageSentPaginatedResponse = messageManager.createJobGetMessagesSent().setServiceRequestParams(
                new ServiceRequestParams()
                        .setPageIndex(4).setResultsPerPage(2)
        ).syncRun(false);
        assertEquals(2, messageSentPaginatedResponse.getEmbedded().getItems().size());
        assertEquals("https://duckduckgo.com/?q=8&ia=about",messageSentPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0).getUrl());
        assertEquals("https://duckduckgo.com/?q=9&ia=about",messageSentPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0).getUrl());

        //test the filtering on messages sent once implemented
        if (false) {
            PaginatedResponse<MessageSent> filteredPaginatedResponse = messageManager.createJobGetMessagesSent().setServiceRequestParams(
                    new ServiceRequestParams()
                            .setPageIndex(1).setResultsPerPage(3).setFilterQuery("(content.contentType='text/html'|content.contentType='image/png')")
            ).syncRun(false);
            assertEquals(3, filteredPaginatedResponse.getEmbedded().getItems().size());

            MessageContentDetail messageContentDetail = filteredPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0);
            assertEquals("https://duckduckgo.com/?q=3&ia=about", messageContentDetail.getUrl());
            assertEquals("text/html", messageContentDetail.getContentType());

            messageContentDetail = filteredPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0);
            assertEquals("https://duckduckgo.com/?q=9&ia=about", messageContentDetail.getUrl());
            assertEquals("text/html", messageContentDetail.getContentType());

            messageContentDetail = filteredPaginatedResponse.getEmbedded().getItems().get(2).getContentList().get(0);
            assertEquals("https://duckduckgo.com/?q=10&ia=about", messageContentDetail.getUrl());
            assertEquals("image/png", messageContentDetail.getContentType());
        }

        //log back in as a friend
        User friendIsBack = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp, tmp));

        PaginatedResponse<MessageReceived> paginatedResponse = messageManager.createJobGetMessagesReceived().setServiceRequestParams(
                new ServiceRequestParams()
                        .setResultsPerPage(3)
                        .setPageIndex(2)
                .sort("id", ServiceRequestParams.Sort.Order.Desc)
        ).syncRun(false);

        assertEquals(3, paginatedResponse.getEmbedded().getItems().size());
        assertEquals(2, paginatedResponse.getPage().getNumber());
        assertEquals(12, paginatedResponse.getPage().getTotalElements());
        assertEquals(4, paginatedResponse.getPage().getTotalPages());
        assertEquals(3, paginatedResponse.getPage().getSize());
        assertNotNull(paginatedResponse.getLinks().getNext());
        assertNotNull(paginatedResponse.getLinks().getPrev());
        assertNotNull(paginatedResponse.getLinks().getSelf());


        assertEquals("https://duckduckgo.com/?q=5&ia=about", paginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0).getUrl());
        assertEquals("https://duckduckgo.com/?q=4&ia=about", paginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0).getUrl());
        assertEquals("https://duckduckgo.com/?q=3&ia=about", paginatedResponse.getEmbedded().getItems().get(2).getContentList().get(0).getUrl());


        //test the filtering on messages received once implemented
        if (false) {
            PaginatedResponse<MessageReceived> filteredPaginatedResponse = messageManager.createJobGetMessagesReceived().setServiceRequestParams(
                    new ServiceRequestParams()
                            .setPageIndex(1).setResultsPerPage(3).setFilterQuery("(contentType='text/html'|contentType='image/png')")
            ).syncRun(false);
            assertEquals(3, filteredPaginatedResponse.getEmbedded().getItems().size());

            MessageContentDetail messageContentDetail = filteredPaginatedResponse.getEmbedded().getItems().get(0).getContentList().get(0);
            assertEquals("https://duckduckgo.com/?q=3&ia=about", messageContentDetail.getUrl());
            assertEquals("text/html", messageContentDetail.getContentType());

            messageContentDetail = filteredPaginatedResponse.getEmbedded().getItems().get(1).getContentList().get(0);
            assertEquals("https://duckduckgo.com/?q=9&ia=about", messageContentDetail.getUrl());
            assertEquals("text/html", messageContentDetail.getContentType());

            messageContentDetail = filteredPaginatedResponse.getEmbedded().getItems().get(2).getContentList().get(0);
            assertEquals("https://duckduckgo.com/?q=10&ia=about", messageContentDetail.getUrl());
            assertEquals("image/png", messageContentDetail.getContentType());
        }



        messageManager.createJobDeleteMessageReceived(paginatedResponse.getEmbedded().getItems().get(0).getId()).syncRun(false);

        messageManager.createJobPostMessageToFriend("text/plain", null, "https://duckduckgo.com/?q=" + counter++ + "&ia=about", friendId).syncRun(false);

        messageManager.createJobPostMessageToFriend(contentType, null, "https://duckduckgo.com/?q=" + counter + "&ia=about", friendId).syncRun(false);
        messageManager.createJobPostMessageToFriend("image/png", null, "https://www.google.com/images/srpr/logo11w.png", friendId).syncRun(false);

    }




    public void testRequestShareWithFriend() throws Throwable {
        //create a "friend"
        String tmp = String.valueOf(System.currentTimeMillis());
        User friend = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp, tmp));
        assertNotNull(friend);
        Log.i(TAG, "friend created!!");
        final long friendId = friend.getId();

        //create a local user
        User self = TestUtilities.registerUserSynchronous(this);
        Log.i(TAG, "self created!!");
        assertNotNull(self);
        assertTrue(friendId != self.getId());

        //share to a friend
        final String contentType = "text/html";
        final String url = "http://www.google.com";

        final LatchedBusListener<EventMessageSentPosted> latchedBusListener = new LatchedBusListener<>(EventMessageSentPosted.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        JobPostMessage jobShareWithFriend = messageManager.createJobPostMessageToFriend(contentType, null, url, friendId);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobShareWithFriend);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        assertNotNull(latchedBusListener.getEvent());
        assertNull(latchedBusListener.getEvent().getFailure());
        MessageContentDetail sentMessageContentDetail = latchedBusListener.getEvent().getSuccess().getContentList().get(0);
        assertEquals(contentType, sentMessageContentDetail.getContentType());
        assertEquals(url, sentMessageContentDetail.getUrl());

        //log back in as a friend
        friend = TestUtilities.registerUserSynchronous(this, new PasswordUserCredential(tmp, tmp));
        assertNotNull(friend);
        Log.i(TAG, "logged back in as a friend!!");
        final LatchedBusListener<EventMessagesReceived> latchedEventMessagesReceivedBusListener = new LatchedBusListener<>(EventMessagesReceived.class);

        ServiceSupport.Instance.getEventBus().register(latchedEventMessagesReceivedBusListener);
        JobGetMessagesReceived jobRefreshMessagesReceived = messageManager.createJobGetMessagesReceived();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRefreshMessagesReceived);
        latchedEventMessagesReceivedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedEventMessagesReceivedBusListener);

        assertNotNull(latchedEventMessagesReceivedBusListener.getEvent());
        assertNull(latchedEventMessagesReceivedBusListener.getEvent().getFailure());
        final List<MessageReceived> messagesReceived = latchedEventMessagesReceivedBusListener.getEvent().getSuccess().getEmbedded().getItems();
        verifyMessageReceived(messagesReceived, self.getId(), self.getUsername(), contentType, url);

        //verify message deletion works
        final LatchedBusListener<EventMessageReceivedDeleted> latchedEventMessageDeletedBusListener = new LatchedBusListener<>(EventMessageReceivedDeleted.class);
        ServiceSupport.Instance.getEventBus().register(latchedEventMessageDeletedBusListener);
        MessageReceived messageReceived = messagesReceived.get(0);
        JobDeleteMessageReceived jobDeleteMessageReceived = messageManager.createJobDeleteMessageReceived(messageReceived.getId());
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobDeleteMessageReceived);

        //this will show we're ok locally
        latchedEventMessageDeletedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedEventMessageDeletedBusListener);
        Throwable failure = latchedEventMessageDeletedBusListener.getEvent().getFailure();
        assertNull(failure);

        //this will verify that the server has also registered the message deletion
        final LatchedBusListener<EventMessagesReceived> latchedEventEmptyMessagesReceivedBusListener = new LatchedBusListener<>(EventMessagesReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedEventEmptyMessagesReceivedBusListener);
        jobRefreshMessagesReceived = messageManager.createJobGetMessagesReceived();
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobRefreshMessagesReceived);
        latchedEventEmptyMessagesReceivedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedEventEmptyMessagesReceivedBusListener);
        assertNotNull(latchedEventEmptyMessagesReceivedBusListener.getEvent());
        assertNull(latchedEventEmptyMessagesReceivedBusListener.getEvent().getFailure());
        PaginatedResponse.Embedded<MessageReceived> embedded1 = latchedEventEmptyMessagesReceivedBusListener.getEvent().getSuccess().getEmbedded();
        assertNull(embedded1);
    }

    private void verifyMessageReceived(List<MessageReceived> messagesReceived, long expectedSenderId, String expectedSenderName, String expectedContentType, String expectedUrl) {
        assertEquals(1, messagesReceived.size());
        MessageReceived messageReceived = messagesReceived.get(0);
        assertEquals(expectedSenderId, messageReceived.getSender().getId());
        assertEquals(expectedSenderName, messageReceived.getSender().getUsername());
        assertEquals(1, messageReceived.getContentList().size());
        MessageContentDetail messageContentDetail = messageReceived.getContentList().get(0);
        assertEquals(expectedContentType, messageContentDetail.getContentType());
        assertEquals(expectedUrl, messageContentDetail.getUrl());
    }


}
