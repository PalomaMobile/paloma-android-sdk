package com.palomamobile.android.sdk.core;

import junit.framework.TestCase;

import java.util.Map;

public class ServiceRequestParamsUnitTest extends TestCase {

    public void testFilterQuery() {
        ServiceRequestParams params = new ServiceRequestParams();
        final String q = "type = 'a' or type = 'b'";
        params.setFilterQuery(q);
        assertEquals(q, params.getFilterQuery());
        String query = params.getFilterQuery();
        assertNotNull(query);
        assertEquals(q, query);
    }

    public void testPagination() {
        ServiceRequestParams params = new ServiceRequestParams();
        params.setPageIndex(2);
        assertEquals(2, params.getPageIndex());
        assertEquals(ServiceRequestParams.DEFAULT_RESULTS_PER_PAGE, params.getResultsPerPage());

        params.setPageIndex(1);
        params.setResultsPerPage(10);
        assertEquals(1, params.getPageIndex());
        assertEquals(10, params.getResultsPerPage());

        Map<String, String> map = params.getOptions();
        String page = map.get("page");
        assertNotNull(page);
        assertEquals(1, Integer.parseInt(page));

        String size = map.get("size");
        assertNotNull(size);
        assertEquals(10, Integer.parseInt(size));
    }

    public void testSort() {
        ServiceRequestParams params = new ServiceRequestParams();
        params.sort("b", ServiceRequestParams.Sort.Order.Asc)
                .sort("a", ServiceRequestParams.Sort.Order.Desc)
                .sort("c", ServiceRequestParams.Sort.Order.Asc);
        String[] sortParams = params.getSortParams();
        assertEquals(3, sortParams.length);
        assertEquals("b,ASC", sortParams[0]);
        assertEquals("a,DESC", sortParams[1]);
        assertEquals("c,ASC", sortParams[2]);
    }

}
