package com.palomamobile.android.sdk.verification;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;

class VerificationManager implements IVerificationManager {

    private IVerificationService verificationService;

    public VerificationManager(IServiceSupport serviceSupport) {
        this.verificationService = serviceSupport.getRestAdapter().create(IVerificationService.class);
        serviceSupport.getInternalEventBus().register(this);
        serviceSupport.registerServiceManager(IVerificationManager.class, this);
    }

    @Override
    @NonNull
    public IVerificationService getService() {
        return verificationService;
    }
}
