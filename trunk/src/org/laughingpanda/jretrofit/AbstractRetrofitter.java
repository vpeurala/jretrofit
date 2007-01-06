/*
 * Copyright 2006 Ville Peurala
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
