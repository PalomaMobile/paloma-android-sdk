package com.palomamobile.android.sdk.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PaginatedResponseUnitTest extends TestCase {


    private String jsonText = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"items\": [\n" +
            "      {\n" +
            "        \"id\": 100\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 101\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8083/users/189/messages/received?{&page,size,sort}\",\n" +
            "      \"templated\": true\n" +
            "    },\n" +
            "    \"next\": {\n" +
            "      \"href\": \"http://localhost:8083/users/189/messages/received?{&page=1&size=20{&sort}\",\n" +
            "      \"templated\": false\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"totalPages\": 2,\n" +
            "    \"totalElements\": 28,\n" +
            "    \"number\": 0,\n" +
            "    \"size\": 20\n" +
            "  }\n" +
            "}";


    public void testFromJson() {
        Gson gson = new Gson();
        Type type = new TypeToken<PaginatedResponse<TestMessage>>() {}.getType();

        PaginatedResponse<TestMessage> response = gson.fromJson(jsonText, type);
        assertNotNull(response);
        assertNotNull(response.getEmbedded());
        assertNotNull(response.getEmbedded().getItems());
        assertEquals(2, response.getEmbedded().getItems().size());
        assertEquals("http://localhost:8083/users/189/messages/received?{&page,size,sort}", response.getLinks().getSelf().getHref());
        assertEquals(true, response.getLinks().getSelf().isTemplated());
        assertEquals("http://localhost:8083/users/189/messages/received?{&page=1&size=20{&sort}", response.getLinks().getNext().getHref());
        assertEquals(false, response.getLinks().getNext().isTemplated());


        PaginatedResponse<TestMessage> expected = new PaginatedResponse<>();

        //expected embedded
        PaginatedResponse.Embedded<TestMessage> embedded = new PaginatedResponse.Embedded<>();
        TestMessage item0 = new TestMessage();
        item0.setId(100);
        TestMessage item1 = new TestMessage();
        item1.setId(101);
        List<TestMessage> items = new ArrayList<>(2);
        items.add(item0);
        items.add(item1);
        embedded.setItems(items);
        expected.setEmbedded(embedded);

        //expected links
        PaginatedResponse.Links links = new PaginatedResponse.Links();
        PaginatedResponse.Link self = new PaginatedResponse.Link();
        self.setHref("http://localhost:8083/users/189/messages/received?{&page,size,sort}");
        self.setTemplated(true);
        links.setSelf(self);
        PaginatedResponse.Link next = new PaginatedResponse.Link();
        next.setHref("http://localhost:8083/users/189/messages/received?{&page=1&size=20{&sort}");
        next.setTemplated(false);
        links.setNext(next);
        expected.setLinks(links);


        //expected page
        PaginatedResponse.Page page = new PaginatedResponse.Page();
        page.setTotalPages(2);
        page.setTotalElements(28);
        page.setNumber(0);
        page.setSize(20);

        assertFalse(expected.equals(response));
        expected.setPage(page);
        assertTrue(expected.equals(response));

        expected.getEmbedded().getItems().add(new TestMessage());
        assertFalse(expected.equals(response));

        expected.getEmbedded().getItems().remove(2);
        assertEquals(expected, response);

        expected.getEmbedded().getItems().get(0).setId(99);
        assertFalse(expected.equals(response));

        expected.getEmbedded().getItems().get(0).setId(100);
        assertEquals(expected, response);
    }

    private static class TestMessage {
        private long id;

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestMessage that = (TestMessage) o;

            return id == that.id;

        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("TestMessage{");
            sb.append("id=").append(id);
            sb.append('}');
            return sb.toString();
        }
    }
}
