package org.laughingpanda.jretrofit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Ville Peurala
 */
public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.laughingpanda.jretrofit");
        //$JUnit-BEGIN$
        suite.addTestSuite(RetrofitterCreationTest.class);
        suite.addTestSuite(RetrofitWithoutMethodCachingTest.class);
        suite.addTestSuite(RetrofitWithMethodCachingTest.class);
        //$JUnit-END$
        return suite;
    }
}
