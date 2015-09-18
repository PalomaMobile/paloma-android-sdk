package com.palomamobile.android.sdk.core.util;

import junit.framework.TestCase;

/**
 *
 */
public class LatchedBusListenerTest extends TestCase {

    public void testCore() {
        Class typeA = A.class;
        Class typeB = B.class;

        A a = new A();
        B b = new B();
        C c = new C();

        assertTrue(typeA.isAssignableFrom(a.getClass()));
        assertTrue(typeA.isAssignableFrom(b.getClass()));
        assertTrue(typeA.isAssignableFrom(c.getClass()));

        assertFalse(typeB.isAssignableFrom(a.getClass()));
        assertFalse(typeB.isAssignableFrom(c.getClass()));
    }

    class A {}

    class B extends A {}

    class C extends A {}
}
