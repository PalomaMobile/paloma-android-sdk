package com.palomamobile.android.sdk.auth;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 *
 */
public class AuthManagerInstrumentationTest extends InstrumentationTestCase {

    public void testGetClientAccessToken() {
        ServiceSupport.Instance.init(getInstrumentation().getContext());
        IAuthManager authManager = ServiceSupport.Instance.getServiceManager(IAuthManager.class);

        assertNotNull(authManager);

        ServiceSupport.Instance.getCache().clear();

        AccessToken clientToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY, null);
        assertNull(clientToken);

        clientToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_THEN_NETWORK, Long.toString(System.currentTimeMillis()));
        assertNotNull(clientToken);

        clientToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY, null);
        assertNotNull(clientToken);

        authManager.clearCachedTokens();
        assertNull(authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY, null));
    }

    //testGetClientAccessToken(){} is effectively done in the User SDK
}
