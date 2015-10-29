package com.palomamobile.android.sdk.verification.email;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;

class EmailVerificationManager implements IEmailVerificationManager {

    private IEmailVerificationService verificationService;

    public EmailVerificationManager(IServiceSupport serviceSupport) {
        this.verificationService = serviceSupport.getRestAdapter().create(IEmailVerificationService.class);
        serviceSupport.registerServiceManager(IEmailVerificationManager.class, this);
    }

    @Override
    @NonNull
    public IEmailVerificationService getService() {
        return verificationService;
    }

}
