package org.laughingpanda.jretrofit;

/**
 * @author Ville Peurala
 */
class RetrofitterWithoutMethodLookupCaching extends AbstractRetrofitter {
    protected AbstractMethodLookupHelper createMethodLookupHelper(Object target) {
        return new NonCachingMethodLookupHelper(target);
    }

    public final Retrofitter withMethodLookupCaching() {
        return new RetrofitterWithMethodLookupCaching();
    }

    public final Retrofitter withoutMethodLookupCaching() {
        return this;
    }
}
