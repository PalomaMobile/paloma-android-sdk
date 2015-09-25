package com.palomamobile.android.sdk.messageThread;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.LatchedBusListener;
import com.palomamobile.android.sdk.user.TestUtilities;
import com.palomamobile.android.sdk.user.User;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Karel Herink
 */
public class MessageThreadManagerInstrumentationTest extends InstrumentationTestCase {
    public static final String TAG = MessageThreadManagerInstrumentationTest.class.getSimpleName();

    private IMessageThreadManager messageThreadManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        messageThreadManager = ServiceSupport.Instance.getServiceManager(IMessageThreadManager.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateNewMessageThread() throws Throwable {
        User self = TestUtilities.registerUserSynchronous(this);

        String tmp = Long.toString(System.currentTimeMillis());
        HashMap<String, String> custom = new HashMap<>();
        custom.put("greeting", "hello");
        custom.put("mood", "positive");
        JobPostMessageThread jobPostMessageThread = messageThreadManager.createJobPostMessageThread(tmp, null, "testType", custom, null);

        final LatchedBusListener<EventMessageThreadPosted> latchedBusListener = new LatchedBusListener<>(EventMessageThreadPosted.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostMessageThread);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(this);

        EventMessageThreadPosted event = latchedBusListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
        assertNotNull(event.getSuccess());

        MessageThread messageThread = event.getSuccess();
        assertEquals(tmp, messageThread.getName());
        assertEquals(2, messageThread.getCustom().size());
        assertEquals("hello", messageThread.getCustom().get("greeting"));
        assertEquals("positive", messageThread.getCustom().get("mood"));
        assertEquals(1, messageThread.getMemberCount());
        assertEquals(0, messageThread.getMessageCount());
        assertEquals(self.getId(), messageThread.getOwner().getId());
        assertEquals(self.getUsername(), messageThread.getOwner().getUsername());
        assertEquals("testType", messageThread.getType());
    }

    public void testGetMessageThread() throws Throwable {
        User self = TestUtilities.registerUserSynchronous(this);
        String tmp = Long.toString(System.currentTimeMillis());
        HashMap<String, String> custom = new HashMap<>();
        custom.put("greeting", "hello");
        custom.put("mood", "positive");

        JobPostMessageThread jobPostMessageThread = messageThreadManager.createJobPostMessageThread(tmp, null, "testType", custom, null);

        final LatchedBusListener<EventMessageThreadPosted> latchedBusListener = new LatchedBusListener<>(EventMessageThreadPosted.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListener);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostMessageThread);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListener);

        EventMessageThreadPosted event = latchedBusListener.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
        assertNotNull(event.getSuccess());
        MessageThread created = event.getSuccess();

        JobGetMessageThread jobGetMessageThread = messageThreadManager.createJobGetMessageThread(created.getId());
        final LatchedBusListener<EventMessageThreadReceived> latchedBusListenerReceive = new LatchedBusListener<>(EventMessageThreadReceived.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerReceive);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobGetMessageThread);
        latchedBusListener.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerReceive);

        EventMessageThreadReceived eventReceive = latchedBusListenerReceive.getEvent();
        assertNotNull(event);
        assertNull(event.getFailure());
        assertNotNull(event.getSuccess());
        MessageThread received = event.getSuccess();
        assertEquals(created, received);


    }

    public void testMessageThreadUpdate() throws Throwable {
        User self = TestUtilities.registerUserSynchronous(this);

        String name = "blah_" + Long.toString(System.currentTimeMillis());
        MessageThread messageThreadBlah = messageThreadManager.createJobPostMessageThread(name, null, "blah", null, null).syncRun(false);

        String tmp = Long.toString(System.currentTimeMillis());
        HashMap<String, String> custom = new HashMap<>();
        custom.put("greeting", "hello");
        custom.put("mood", "positive");
        JobPostMessageThread jobPostMessageThread = messageThreadManager.createJobPostMessageThread(tmp, messageThreadBlah.getId(), "testType", custom, null);

        final LatchedBusListener<EventMessageThreadPosted> latchedBusListenerPost = new LatchedBusListener<>(EventMessageThreadPosted.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerPost);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobPostMessageThread);
        latchedBusListenerPost.await(20, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerPost);

        EventMessageThreadPosted eventPost = latchedBusListenerPost.getEvent();
        assertNotNull(eventPost);
        assertNull(eventPost.getFailure());
        assertNotNull(eventPost.getSuccess());
        final MessageThread posted = eventPost.getSuccess();
        assertEquals(messageThreadBlah.getId(), posted.getRelatedTo().longValue());

        final String tmpUpdate = Long.toString(System.currentTimeMillis());
        final MessageThreadUpdate update = new MessageThreadUpdate();
        update.withName(tmpUpdate).removeRelatedTo().withType("charismatic").withCustom("greeting", "yo").removeCustom("mood").withCustom("feeling", "happy");

        JobUpdateMessageThread jobUpdateMessageThread = messageThreadManager.createJobUpdateMessageThread(posted.getId(), update);
        final LatchedBusListener<EventMessageThreadUpdated> latchedBusListenerUpdate = new LatchedBusListener<>(EventMessageThreadUpdated.class);
        ServiceSupport.Instance.getEventBus().register(latchedBusListenerUpdate);
        ServiceSupport.Instance.getJobManager().addJobInBackground(jobUpdateMessageThread);
        latchedBusListenerUpdate.await(10, TimeUnit.SECONDS);
        ServiceSupport.Instance.getEventBus().unregister(latchedBusListenerUpdate);

        EventMessageThreadUpdated eventUpdate = latchedBusListenerUpdate.getEvent();
        assertNotNull(eventUpdate);
        assertNull(eventUpdate.getFailure());
        MessageThread updated = eventUpdate.getSuccess();
        assertNotNull(updated);
        assertEquals(posted.getId(), updated.getId());
        assertEquals(posted.getMemberCount(), updated.getMemberCount());
        assertEquals(posted.getMessageCount(), updated.getMessageCount());
        assertEquals(posted.getOwner(), updated.getOwner());

        assertNull(updated.getRelatedTo()); //both null
        assertEquals(tmpUpdate, updated.getName());
        assertEquals("charismatic", updated.getType());
        assertNull(updated.getCustom().get("mood"));
        assertEquals("yo", updated.getCustom().get("greeting"));
        assertEquals("happy", updated.getCustom().get("feeling"));

        //XXX put this back once Craig fixes the server
//        assertEquals(2, updated.getCustom().size());
    }

}
