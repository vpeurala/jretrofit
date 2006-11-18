package org.laughingpanda.jretrofit;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ville Peurala
 */
class RetrofitterWithMethodLookupCaching implements Retrofitter {
    public Object partial(Object target, Class interfaceToImplement) {
        return partial(target, new Class[] { interfaceToImplement });
    }

    public Object partial(Object target, Class[] interfacesToImplement) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                allInterfacesToImplement(target, interfacesToImplement),
                new RetrofitInvocationHandler(target, true));
    }

    public Retrofitter withMethodLookupCaching() {
        return this;
    }

    public Retrofitter withoutMethodLookupCaching() {
        return new RetrofitterWithoutMethodLookupCaching();
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
