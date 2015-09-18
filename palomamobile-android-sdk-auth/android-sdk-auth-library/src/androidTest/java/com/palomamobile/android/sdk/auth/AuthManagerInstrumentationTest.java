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

        AccessToken clientToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY);
        assertNull(clientToken);

        clientToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_THEN_NETWORK);
        assertNotNull(clientToken);

        clientToken = authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY);
        assertNotNull(clientToken);

        authManager.clearCachedTokens();
        assertNull(authManager.getClientAccessToken(IAuthManager.TokenRetrievalMode.CACHE_ONLY));
    }

    //testGetClientAccessToken(){} is effectively done in the User SDK
}
