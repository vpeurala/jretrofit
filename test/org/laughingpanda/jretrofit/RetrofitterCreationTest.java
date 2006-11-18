package org.laughingpanda.jretrofit;

import junit.framework.TestCase;

/**
 * @author Ville Peurala / Reaktor Innovations
 */
public class RetrofitterCreationTest extends TestCase {
    public void testStaticClassRetrofitCanCreateCachingRetrofitter() {
        Object created = Retrofit.withMethodLookupCaching();
        assertTrue(created instanceof RetrofitterWithMethodLookupCaching);
    }

    public void testStaticClassRetrofitCanCreateNonCachingRetrofitter() {
        Object created = Retrofit.withoutMethodLookupCaching();
        assertTrue(created instanceof RetrofitterWithoutMethodLookupCaching);
    }

    public void testRetrofitterWithMethodLookupCachingReturnsItselfFromMethodWithMethodLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithMethodLookupCaching();
        assertSame(retrofitter, retrofitter.withMethodLookupCaching());
    }

    public void testRetrofitterWithMethodLookupCachingReturnsANewNonCachingInstanceFromMethodWithoutLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithMethodLookupCaching();
        assertNotSame(retrofitter, retrofitter.withoutMethodLookupCaching());
        assertTrue(retrofitter.withoutMethodLookupCaching() instanceof RetrofitterWithoutMethodLookupCaching);
    }

    public void testRetrofitterWithoutMethodLookupCachingReturnsItselfFromMethodWithoutMethodLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithoutMethodLookupCaching();
        assertSame(retrofitter, retrofitter.withoutMethodLookupCaching());
    }

    public void testRetrofitterWithoutMethodLookupCachingReturnsANewCachingInstanceFromMethodWithLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithoutMethodLookupCaching();
        assertNotSame(retrofitter, retrofitter.withMethodLookupCaching());
        assertTrue(retrofitter.withMethodLookupCaching() instanceof RetrofitterWithMethodLookupCaching);
    }
}
