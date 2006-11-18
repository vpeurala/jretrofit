package org.laughingpanda.jretrofit;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ville Peurala
 */
class RetrofitterWithoutMethodLookupCaching implements Retrofitter {
    public Object complete(Object target, Class interfaceToImplement) {
        // FIXME VP Not implemented.
        throw new UnsupportedOperationException("complete");
    }

    public Object complete(Object target, Class[] interfacesToImplement) {
        // FIXME VP Not implemented.
        throw new UnsupportedOperationException("complete");
    }

    public Object partial(Object target, Class interfaceToImplement) {
        return partial(target, new Class[] { interfaceToImplement });
    }

    public Object partial(Object target, Class[] interfacesToImplement) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                allInterfacesToImplement(target, interfacesToImplement),
                new RetrofitInvocationHandler(target, false));
    }

    public Retrofitter withMethodLookupCaching() {
        // FIXME VP Not implemented.
        throw new UnsupportedOperationException("withMethodLookupCaching");
    }

    public Retrofitter withoutMethodLookupCaching() {
        // FIXME VP Not implemented.
        throw new UnsupportedOperationException("withoutMethodLookupCaching");
    }

    private static Class[] allInterfacesToImplement(Object target,
            Class[] interfacesToImplement) {
        ArrayList allInterfacesToImplement = new ArrayList();
        allInterfacesToImplement.addAll(Arrays.asList(interfacesToImplement));
        allInterfacesToImplement.addAll(Arrays.asList(target.getClass()
                .getInterfaces()));
        return (Class[]) allInterfacesToImplement
                .toArray(new Class[allInterfacesToImplement.size()]);
    }
}
