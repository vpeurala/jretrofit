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
import java.util.List;

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

    private void checkParameters(Object target, Class[] interfacesToImplement) {
        if (target == null) {
            throw new IllegalArgumentException("Target object cannot be null!");
        }
        if (interfacesToImplement == null) {
            throw new IllegalArgumentException(
                    "Array of interfaces to implement cannot be null!");
        }
        for (int i = 0; i < interfacesToImplement.length; i++) {
            if (interfacesToImplement[i] == null) {
                throw new IllegalArgumentException(
                        "Interface to implement cannot be null!");
            }
        }
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
        checkParameters(target, interfacesToImplement);
        AbstractMethodLookupHelper helper = createMethodLookupHelper(target);
        checkThatAllRequiredMethodsAreImplemented(interfacesToImplement, helper);
        return createProxy(target, interfacesToImplement, helper);
    }

    protected abstract AbstractMethodLookupHelper createMethodLookupHelper(
            Object target);

    private Object createProxy(Object target, Class[] interfacesToImplement,
            AbstractMethodLookupHelper methodLookupHelper) {
        ClassLoader[] candidateClassLoaders = getCandidateClassLoaders(target,
                interfacesToImplement);
        List exceptions = new ArrayList();
        for (int i = 0; i < candidateClassLoaders.length; i++) {
            try {
                return Proxy
                        .newProxyInstance(candidateClassLoaders[i],
                                allInterfacesToImplement(target,
                                        interfacesToImplement),
                                new RetrofitInvocationHandler(
                                        methodLookupHelper));
            } catch (IllegalArgumentException e) {
                // The classloader used cannot load all the required classes,
                // store the exception and try the next one...
                exceptions.add(e);
            }
        }
        // Classloaders exhausted... throw an exception.
        throw new RuntimeException(
                "Could not find a suitable classloader for retrofitting! Exceptions when attempting to create proxy: "
                        + exceptions);
    }

    private ClassLoader[] getCandidateClassLoaders(Object target,
            Class[] interfacesToImplement) {
        ClassLoader[] classLoaders = new ClassLoader[interfacesToImplement.length + 1];
        classLoaders[0] = target.getClass().getClassLoader();
        for (int i = 0; i < interfacesToImplement.length; i++) {
            classLoaders[i + 1] = interfacesToImplement[i].getClassLoader();
        }
        return classLoaders;
    }

    public final Object partial(Object target, Class interfaceToImplement) {
        return partial(target, new Class[] { interfaceToImplement });
    }
    public final Object partial(Object target, Class[] interfacesToImplement) {
        checkParameters(target, interfacesToImplement);
        return createProxy(target, interfacesToImplement,
                createMethodLookupHelper(target));
    }
}
