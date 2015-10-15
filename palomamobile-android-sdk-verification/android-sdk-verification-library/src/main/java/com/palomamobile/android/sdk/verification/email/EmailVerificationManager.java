package com.palomamobile.android.sdk.verification.email;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.Utilities;

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

    @Override
    public JobCreateEmailVerification createJobCreateEmailVerification(String emailAddress) {
        return new JobCreateEmailVerification(emailAddress);
    }

    @Override
    public JobUpdateEmailVerification createJobUpdateEmailVerification(String emailAddress, String code) {
        String appName = Utilities.getAppNameFromMetadata(ServiceSupport.Instance.getContext());
        return new JobUpdateEmailVerification(emailAddress, code, appName);
    }
}
