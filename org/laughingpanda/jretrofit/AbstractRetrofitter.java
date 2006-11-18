package org.laughingpanda.jretrofit;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ville Peurala
 */
abstract class AbstractRetrofitter implements Retrofitter {
    private Class[] allInterfacesToImplement(Object target,
            Class[] interfacesToImplement) {
        ArrayList allInterfacesToImplement = new ArrayList();
        allInterfacesToImplement.addAll(Arrays.asList(interfacesToImplement));
        allInterfacesToImplement.addAll(Arrays.asList(target.getClass()
                .getInterfaces()));
        return (Class[]) allInterfacesToImplement
                .toArray(new Class[allInterfacesToImplement.size()]);
    }

    private void checkThatAllRequiredMethodsAreImplemented(
            Class[] interfacesToImplement, AbstractMethodLookupHelper helper) {
        ArrayList allMethodsWhichShouldBeImplementedList = new ArrayList();
        for (int i = 0; i < interfacesToImplement.length; i++) {
            allMethodsWhichShouldBeImplementedList.addAll(Arrays
                    .asList(interfacesToImplement[i].getMethods()));
        }
        Method[] allMethodsWhichShouldBeImplemented = (Method[]) allMethodsWhichShouldBeImplementedList
                .toArray(new Method[allMethodsWhichShouldBeImplementedList
                        .size()]);
        ArrayList methodsNotImplemented = new ArrayList();
        for (int i = 0; i < allMethodsWhichShouldBeImplemented.length; i++) {
            try {
                helper.findMethodToCall(allMethodsWhichShouldBeImplemented[i]);
            } catch (UnsupportedOperationException e) {
                methodsNotImplemented
                        .add(allMethodsWhichShouldBeImplemented[i]);
            }
        }
        if (!methodsNotImplemented.isEmpty()) {
            throw new AllMethodsNotImplementedException(
                    (Method[]) methodsNotImplemented
                            .toArray(new Method[methodsNotImplemented.size()]));
        }
    }

    public final Object complete(Object target, Class interfaceToImplement) {
        return complete(target, new Class[] { interfaceToImplement });
    }

    public final Object complete(Object target, Class[] interfacesToImplement) {
        AbstractMethodLookupHelper helper = createMethodLookupHelper(target);
        checkThatAllRequiredMethodsAreImplemented(interfacesToImplement, helper);
        return createProxy(target, interfacesToImplement, helper);
    }

    protected abstract AbstractMethodLookupHelper createMethodLookupHelper(
            Object target);

    private Object createProxy(Object target, Class[] interfacesToImplement,
            AbstractMethodLookupHelper methodLookupHelper) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                allInterfacesToImplement(target, interfacesToImplement),
                new RetrofitInvocationHandler(methodLookupHelper));
    }

    public final Object partial(Object target, Class interfaceToImplement) {
        return partial(target, new Class[] { interfaceToImplement });
    }
    public final Object partial(Object target, Class[] interfacesToImplement) {
        return createProxy(target, interfacesToImplement,
                createMethodLookupHelper(target));
    }
}
