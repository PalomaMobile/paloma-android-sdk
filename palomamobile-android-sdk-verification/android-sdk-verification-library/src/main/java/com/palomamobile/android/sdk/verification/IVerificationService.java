package com.palomamobile.android.sdk.verification;

import com.google.gson.JsonObject;
import com.palomamobile.android.sdk.auth.IAuthManager;
import com.palomamobile.android.sdk.core.CustomHeader;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * This interface is consumed by the Retrofit library to provide access to the Paloma Mobile Platform Verification Service RESTful API.
 * All calls are synchronous. To get a concrete implementation of this interface call {@link IVerificationManager#getService()}
 * This class provides somewhat low level access to the Server API it may be more convenient to use methods
 * in {@link IVerificationManager}.
 * <br/>
 * @see <a href="http://54.251.112.144/docs/verification-service/index.html">Paloma Mobile Platform Verification Service RESTful API</a>
 */
public interface IVerificationService {

}
