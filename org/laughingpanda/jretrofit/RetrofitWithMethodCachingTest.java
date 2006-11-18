package org.laughingpanda.jretrofit;

import java.lang.reflect.Constructor;

/**
 * @author Ville Peurala
 */
public class RetrofitWithMethodCachingTest extends AbstractRetrofitTestCase {
    protected Retrofitter createRetrofitter() {
        return Retrofit.withMethodLookupCaching();
    }

    public void testCachedMethodCallShouldWorkInSameWayAsUncached() {
        assertEquals("Antti", human.getName());
        assertEquals("Antti", human.getName());
    }

    public void testCallPrivateConstructorForBetterTestCoverage()
            throws Exception {
        // This is a minor detail, but it's irritating
        // to have under 100% unit test coverage just 
        // because the constructor of class Retrofit is
        // private (it is a library class, never meant to
        // be instantiated). So we call it here.
        Constructor c = Retrofit.class.getDeclaredConstructor(null);
        c.setAccessible(true);
        Retrofit retrofit = (Retrofit) c.newInstance(null);
        assertNotNull(retrofit);
    }
}
