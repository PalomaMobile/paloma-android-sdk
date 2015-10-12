package com.palomamobile.android.sdk.core;

import android.test.InstrumentationTestCase;
import com.squareup.otto.Bus;

/**
 * Created by Karel Herink
 */
public class ServiceSupportConfigurationTest extends InstrumentationTestCase {

    public void testOtto() {
        ServiceSupportConfiguration configuration = new ServiceSupportConfiguration(getInstrumentation().getContext());
        configuration.setEventBus(new OttoEventBusAdapter(new Bus()));
        ServiceSupport.Instance.init(configuration);

        IEventBus bus = ServiceSupport.Instance.getEventBus();
        assertTrue(bus instanceof OttoEventBusAdapter);
    }

}
