package com.palomamobile.android.sdk.core;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response that contains a single page from a paginated list of results.
 *
 *
 */
@SuppressWarnings("unused")
public class PaginatedResponse<T> {

    @SerializedName("_links")
    private Links links;

    private Page page;

    @SerializedName("_embedded")
    private Embedded<T> embedded;

    public Embedded<T> getEmbedded() {
        return embedded;
    }

    //for test code
    void setEmbedded(Embedded<T> embedded) {
        this.embedded = embedded;
    }

    public Links getLinks() {
        return links;
    }

    //for test code
    void setLinks(Links links) {
        this.links = links;
    }

    public Page getPage() {
        return page;
    }

    //for test code
    void setPage(Page page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaginatedResponse<?> that = (PaginatedResponse<?>) o;

        if (links != null ? !links.equals(that.links) : that.links != null) return false;
        if (page != null ? !page.equals(that.page) : that.page != null) return false;
        return !(embedded != null ? !embedded.equals(that.embedded) : that.embedded != null);

    }

    @Override
    public int hashCode() {
        int result = links != null ? links.hashCode() : 0;
        result = 31 * result + (page != null ? page.hashCode() : 0);
        result = 31 * result + (embedded != null ? embedded.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaginatedResponse{");
        sb.append("embedded=").append(embedded);
        sb.append(", links=").append(links);
        sb.append(", page=").append(page);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Meta data describing the result page.
     */
    @SuppressWarnings("unused")
    public static class Page {
        /**
         * Page size as requested by the client.
         */
        private int size;
        /**
         * Total number of elements across all pages.
         */
        private int totalElements;
        /**
         * Total number of pages.
         */
        private int totalPages;
        /**
         * Page index starting at zero.
         */
        private int number;

        public int getNumber() {
            return number;
        }

        //for test code
        void setNumber(int number) {
            this.number = number;
        }

        public int getSize() {
            return size;
        }

        //for test code
        void setSize(int size) {
            this.size = size;
        }

        public int getTotalElements() {
            return totalElements;
        }

        //for test code
        void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        //for test code
        void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Page that = (Page) o;

            if (size != that.size) return false;
            if (totalElements != that.totalElements) return false;
            if (totalPages != that.totalPages) return false;
            return number == that.number;
        }

        @Override
        public int hashCode() {
            int result = size;
            result = 31 * result + totalElements;
            result = 31 * result + totalPages;
            result = 31 * result + number;
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("PageMetadata{");
            sb.append("number=").append(number);
            sb.append(", size=").append(size);
            sb.append(", totalElements=").append(totalElements);
            sb.append(", totalPages=").append(totalPages);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Links to available pages relative to the current page (as defined by HATEOAS).
     * @see com.palomamobile.android.sdk.core.PaginatedResponse.Link
     */
    @SuppressWarnings("unused")
    public static class Links {
        private Link self;
        private Link next;
        private Link prev;

        /**
         * @return link to the next page in the current result set (relative to the current page as defined by {@link #getSelf()}).
         */
        public Link getNext() {
            return next;
        }

        /**
         * @return link to the previous page in the current result set (relative to the current page as defined by {@link #getSelf()}).
         */
        public Link getPrev() {
            return prev;
        }

        /**
         * @return link to the current page.
         */
        public Link getSelf() {
            return self;
        }


        //for test code
        void setNext(Link next) {
            this.next = next;
        }

        //for test code
        void setPrev(Link prev) {
            this.prev = prev;
        }

        //for test code
        void setSelf(Link self) {
            this.self = self;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Links links = (Links) o;

            if (self != null ? !self.equals(links.self) : links.self != null) return false;
            if (next != null ? !next.equals(links.next) : links.next != null) return false;
            return !(prev != null ? !prev.equals(links.prev) : links.prev != null);

        }

        @Override
        public int hashCode() {
            int result = self != null ? self.hashCode() : 0;
            result = 31 * result + (next != null ? next.hashCode() : 0);
            result = 31 * result + (prev != null ? prev.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Links{" +
                    "next=" + next +
                    ", self=" + self +
                    ", prev=" + prev +
                    '}';
        }
    }

    /**
     * Link optionally templated per RFC 6570.
     */
    @SuppressWarnings("unused")
    public static class Link {

        private String href;
        private boolean templated;

        /**
         * @return url the link points to
         */
        public String getHref() {
            return href;
        }

        //for test code
        void setHref(String href) {
            this.href = href;
        }

        /**
         * @return true is link is templated as defined by RFC 6570, false otherwise.
         */
        public boolean isTemplated() {
            return templated;
        }

        //for test code
        void setTemplated(boolean templated) {
            this.templated = templated;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Link link = (Link) o;

            if (templated != link.templated) return false;
            return !(href != null ? !href.equals(link.href) : link.href != null);

        }

        @Override
        public int hashCode() {
            int result = href != null ? href.hashCode() : 0;
            result = 31 * result + (templated ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Link{");
            sb.append("href='").append(href).append('\'');
            sb.append(", templated=").append(templated);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * The actual embedded content data.
     * @param <T>
     */
    @SuppressWarnings("unused")
    public static class Embedded<T> {
        private List<T> items;

        /**
         * @return list of embedded result items.
         */
        public List<T> getItems() {
            return items;
        }

        //for test code
        void setItems(List<T> items) {
            this.items = items;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Embedded<?> embedded = (Embedded<?>) o;

            return !(items != null ? !items.equals(embedded.items) : embedded.items != null);

        }

        @Override
        public int hashCode() {
            return items != null ? items.hashCode() : 0;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Embedded{");
            sb.append("items=").append(items);
            sb.append('}');
            return sb.toString();
        }
    }

}
