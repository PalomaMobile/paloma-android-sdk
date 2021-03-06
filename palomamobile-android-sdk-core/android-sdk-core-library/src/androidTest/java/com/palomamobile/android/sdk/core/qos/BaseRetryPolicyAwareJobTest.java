package com.palomamobile.android.sdk.core.qos;

import android.test.InstrumentationTestCase;
import com.path.android.jobqueue.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BaseRetryPolicyAwareJobTest extends InstrumentationTestCase {

    private static final Logger logger = LoggerFactory.getLogger(BaseRetryPolicyAwareJobTest.class);

    public void testConcurrentJobCreationIdUniqueness() throws Throwable {
        int count = 100;
        logger.debug("testConcurrentJobCreationIdUniqueness()");
        ConcreteJobA[] arrA = new ConcreteJobA[count];
        for (int i = 0; i < count; i++) {
            arrA[i] = new ConcreteJobA();
        }
        for (int i = 0; i < arrA.length; i++) {
            ConcreteJobA self = arrA[i];
            for (int j = 0; j < arrA.length; j++) {
                ConcreteJobA other = arrA[j];
                if (i != j) {
                    logger.debug("Compare " + self.getRetryId() + " to " + other.getRetryId());
                    assertFalse(self.getRetryId().equals(other.getRetryId()));
                }
            }
        }

        ConcreteJobB[] arrB = new ConcreteJobB[count];
        for (int i = 0; i < count; i++) {
            arrB[i] = new ConcreteJobB();
            String lastRetryId = arrB[i].getRetryId();
            assertEquals(count + i, Long.parseLong(lastRetryId.substring(lastRetryId.lastIndexOf(':') +1)));
        }
        for (int i = 0; i < arrB.length; i++) {
            ConcreteJobB self = arrB[i];
            for (int j = 0; j < arrB.length; j++) {
                ConcreteJobB other = arrB[j];
                if (i != j) {
                    logger.debug("Compare " + self.getRetryId() + " to " + other.getRetryId());
                    assertFalse(self.getRetryId().equals(other.getRetryId()));
                }
            }
        }

        for (ConcreteJobB self : arrB) {
            for (ConcreteJobA other : arrA) {
                logger.debug("Compare " + self.getRetryId() + " to " + other.getRetryId());
                assertFalse(self.getRetryId().equals(other.getRetryId()));
            }
        }
    }

    public void testSerialization() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        ConcreteJobA before = new ConcreteJobA();
        outputStream.writeObject(before);
        outputStream.flush();
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ConcreteJobA after = (ConcreteJobA) inputStream.readObject();
        logger.debug("before serialization: " + before + " after:" + after);
        assertTrue(before.getRetryId().equals(after.getRetryId()));
        assertTrue(before.equals(after));
    }


    private static class ConcreteJobA extends BaseRetryPolicyAwareJob<Void> {
        public ConcreteJobA() {
            super(new Params(0));
        }

        @Override
        protected void postFailure(Throwable throwable) {
        }

        @Override
        public Void syncRun(boolean postEvent) throws Throwable {
            return null;
        }
    }
    private static class ConcreteJobB extends BaseRetryPolicyAwareJob<Void> {
        public ConcreteJobB() {
            super(new Params(0));
        }

        @Override
        protected void postFailure(Throwable throwable) {
        }

        @Override
        public Void syncRun(boolean postEvent) throws Throwable {
            return null;
        }
    }

}
