package com.palomamobile.android.sdk.verification.email;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.user.IUserManager;

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
    public JobPostEmailVerificationUpdate createJobUpdateEmailVerification(String emailAddress, String code) {
        return new JobPostEmailVerificationUpdate(emailAddress, code);
    }

    @Override
    public JobPostUserVerifiedEmail createJobPostUserVerifiedEmail(String emailAddress, String code) {
        long userId = ServiceSupport.Instance.getServiceManager(IUserManager.class).getUser().getId();
        return new JobPostUserVerifiedEmail(userId, emailAddress, code);
    }

}
