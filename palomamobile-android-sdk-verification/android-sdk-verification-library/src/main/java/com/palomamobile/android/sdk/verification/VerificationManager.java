package com.palomamobile.android.sdk.verification;

import android.support.annotation.NonNull;
import com.palomamobile.android.sdk.core.IServiceSupport;
import com.palomamobile.android.sdk.core.ServiceSupport;
import com.palomamobile.android.sdk.core.util.Utilities;

class VerificationManager implements IVerificationManager {

    private IVerificationService verificationService;

    public VerificationManager(IServiceSupport serviceSupport) {
        this.verificationService = serviceSupport.getRestAdapter().create(IVerificationService.class);
        serviceSupport.registerServiceManager(IVerificationManager.class, this);
    }

    @Override
    @NonNull
    public IVerificationService getService() {
        return verificationService;
    }

    @Override
    public JobCreateEmailVerification createJobCreateEmailVerification(String emailAddress) {
        return new JobCreateEmailVerification(emailAddress);
    }

    @Override
    public JobUpdateEmailVerification createJobUpdateEmailVerification(String emailAddress, String code) {
        String appName = Utilities.getValueFromAppMetadata(ServiceSupport.Instance.getContext(), Utilities.CONFIG_NAME_CLIENT_ID);
        return new JobUpdateEmailVerification(emailAddress, code, appName);
    }
}
