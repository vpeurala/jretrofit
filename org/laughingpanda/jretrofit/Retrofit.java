package org.laughingpanda.jretrofit;

/**
 * @author Ville Peurala
 */
public final class Retrofit {
    public static Object complete(Object target, Class interfaceToImplement) {
        return new RetrofitterWithoutMethodLookupCaching().complete(target,
                interfaceToImplement);
    }

    public static Object complete(Object target, Class[] interfacesToImplement) {
        return new RetrofitterWithoutMethodLookupCaching().complete(target,
                interfacesToImplement);
    }

    /**
     * TODO: write javadoc
     * @param target
     * @param interfaceToImplement
     * @return
     */
    public static Object partial(Object target, Class interfaceToImplement) {
        return new RetrofitterWithoutMethodLookupCaching().partial(target,
                interfaceToImplement);
    }

    /**
     * TODO: write javadoc
     * @param target
     * @param interfacesToImplement
     * @return
     */
    public static Object partial(Object target, Class[] interfacesToImplement) {
        return new RetrofitterWithoutMethodLookupCaching().partial(target,
                interfacesToImplement);
    }

    /**
     * TODO: write javadoc
     * @return
     */
    public static Retrofitter withMethodLookupCaching() {
        return new RetrofitterWithMethodLookupCaching();
    }

    /**
     * TODO: write javadoc
     * @return
     */
    public static Retrofitter withoutMethodLookupCaching() {
        return new RetrofitterWithoutMethodLookupCaching();
    }

    private Retrofit() {
    // Cannot be instantiated.
    }
}
