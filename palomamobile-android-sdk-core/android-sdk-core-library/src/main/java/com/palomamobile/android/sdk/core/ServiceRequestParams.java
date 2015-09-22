package com.palomamobile.android.sdk.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class encapsulates parameters that can be attached to service API requests. Parameters will impact how the
 * results to a request are presented - sort order, pagination, filtering.
 *
 */

public class ServiceRequestParams {

    /**
     * Default page size in a paginated request, i.e. the max number of items in a page.
     */
    public static final int DEFAULT_RESULTS_PER_PAGE = 20;

    private List<Sort> sortOrder = new ArrayList<>();

    private String filterQuery;

    private int pageIndex = 0;
    private int resultsPerPage = DEFAULT_RESULTS_PER_PAGE;

    /**
     * Add "sort-by" attribute and sort direction for the given attribute. Multiple calls to this method retain order.
     * Eg: calling {@code params.sort("lastName", Sort.Order.Asc).sort("firstName", Sort.Order.Desc)} would produce result
     * sorted firstly by last name ascending then by first name descending. Eg: Herink Lukas, Herink Adriana, Walker Ryan, Walker Nathan.
     *
     * @param attribute
     * @param direction
     * @return {@code this} for chaining calls
     */
    public ServiceRequestParams sort(String attribute, Sort.Order direction) {
        sortOrder.add(new Sort(attribute, direction));
        return this;
    }

    /**
     * Requests a specific page number.
     * @param pageIndex
     * @return {@code this} for chaining calls
     */
    public ServiceRequestParams setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    /**
     * @return Requested page number.
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * Request a limit for max number of items in each page.
     * @param resultsPerPage
     * @return {@code this} for chaining calls
     */
    public ServiceRequestParams setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
        return this;
    }

    /**
     * Get requested limit for max number of items in each page.
     * @return
     */
    public int getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * Sort criteria for a request.
     */
    public static class Sort {

        /**
         * Sort order ascending or descending.
         */
        public enum Order {
            /**
             * Ascending sort order.
             */
            Asc {
                @Override
                String toQuerySymbol() {
                    return "ASC";
                }
            },

            /**
             * Descending sort order.
             */
            Desc {
                @Override
                String toQuerySymbol() {
                    return "DESC";
                }
            },
            ;

            abstract String toQuerySymbol();
        }

        String attribute;
        Order order;

        /**
         * Sort by a given attribute in a given order.
         * @param attribute
         * @param order
         */
        public Sort(String attribute, Order order) {
            this.attribute = attribute;
            this.order = order;
        }
    }

    /**
     * @return the filter query.
     */
    public String getFilterQuery() {
        return filterQuery;
    }

    /**
     * Request to filter results based on a query.
     * For documentation on how to build queries see:
     * http://54.251.112.144/index.html#_filtering_results
     *
     * @param filterQuery
     * @return {@code this} for chaining calls
     */
    public ServiceRequestParams setFilterQuery(String filterQuery) {
        this.filterQuery = filterQuery;
        return this;
    }

    /**
     * @return Options for the service request, currently contains only pagination options.
     */
    public Map<String, String> getOptions() {
        Map<String, String> options = new HashMap<>();

        //pagination
        options.put("page", Integer.toString(pageIndex));
        options.put("size", Integer.toString(resultsPerPage));

        return options;
    }

    /**
     * @return Ordered array of sort options.
     * @see #sort(String, Sort.Order)
     */
    public String[] getSortParams() {
        if (sortOrder == null) {
            return null;
        }

        String[] sortParams = new String[sortOrder.size()];
        if (sortOrder.size() > 0) {
            for (int i = 0; i < sortOrder.size(); i++) {
                StringBuilder sortParamStringBuilder = new StringBuilder();
                Sort sort = this.sortOrder.get(i);
                sortParamStringBuilder.append(sort.attribute).append(",").append(sort.order.toQuerySymbol());
                sortParams[i] = sortParamStringBuilder.toString();
            }
        }
        return sortParams;

    }

}
