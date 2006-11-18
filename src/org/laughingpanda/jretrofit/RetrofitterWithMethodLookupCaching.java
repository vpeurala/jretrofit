package org.laughingpanda.jretrofit;

/**
 * @author Ville Peurala
 */
class RetrofitterWithMethodLookupCaching extends AbstractRetrofitter {
    protected AbstractMethodLookupHelper createMethodLookupHelper(Object target) {
        return new CachingMethodLookupHelper(target);
    }

    public Retrofitter withMethodLookupCaching() {
        return this;
    }

    public Retrofitter withoutMethodLookupCaching() {
        return new RetrofitterWithoutMethodLookupCaching();
    }
}
