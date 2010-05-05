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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Ville Peurala
 */
final class RetrofitInvocationHandler implements InvocationHandler,
        Serializable {
    private static final long serialVersionUID = 1977L;
    private final AbstractMethodLookupHelper helper;

    public RetrofitInvocationHandler(AbstractMethodLookupHelper helper) {
        this.helper = helper;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Method targetMethod = helper.findMethodToCall(method);
        try {
            return targetMethod.invoke(helper.getTarget(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
