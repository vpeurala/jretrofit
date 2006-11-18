package net.sf.jretrofit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Ville Peurala
 */
public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sf.jretrofit");
        //$JUnit-BEGIN$
        suite.addTestSuite(RetrofitWithoutMethodCachingTest.class);
        suite.addTestSuite(RetrofitWithMethodCachingTest.class);
        //$JUnit-END$
        return suite;
    }
}
