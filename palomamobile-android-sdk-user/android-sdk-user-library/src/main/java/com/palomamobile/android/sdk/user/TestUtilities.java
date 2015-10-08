package com.palomamobile.android.sdk.user;

import android.test.InstrumentationTestCase;
import com.palomamobile.android.sdk.auth.IUserCredential;
import com.palomamobile.android.sdk.core.ServiceSupport;

/**
 * User creation utilities designed for use in Instrumentation tests.
 */
public class TestUtilities {

    /**
     * Register a {@link User} synchronously using a name and a password based on current system time.
     * @param instrumentationTestCase
     * @return
     * @throws Throwable
     */
    public static User registerUserSynchronous(InstrumentationTestCase instrumentationTestCase) throws Throwable {
        String tmp = String.valueOf(System.currentTimeMillis());
        PasswordUserCredential passwordUserCredential = new PasswordUserCredential(tmp, tmp);
        return registerUserSynchronous(instrumentationTestCase, passwordUserCredential);
    }

    /**
     * Register a {@link User} synchronously using the provided name and password.
     * @param instrumentationTestCase
     * @return
     * @throws Throwable
     */
    public static User registerUserSynchronous(InstrumentationTestCase instrumentationTestCase, final IUserCredential credential) throws Throwable {
        final IUserManager userManager = ServiceSupport.Instance.getServiceManager(IUserManager.class);

        JobRegisterUser jobRegisterUser = userManager.createJobRegisterUser(credential);
        User user = jobRegisterUser.syncRun(true);

        InstrumentationTestCase.assertNotNull(user);
        return user;
    }

}
