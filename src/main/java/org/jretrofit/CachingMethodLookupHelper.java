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
package org.jretrofit;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Ville Peurala
 */
class CachingMethodLookupHelper extends AbstractMethodLookupHelper implements
        Serializable {
    private static final long serialVersionUID = 1977L;
    private final HashMap<Method, Method> methodCache = new HashMap<Method, Method>();

    protected CachingMethodLookupHelper(Object target) {
        super(target);
    }

    @Override
    Method findMethodToCall(Method interfaceMethod) {
        Method cachedMethod = methodCache.get(interfaceMethod);
        if (cachedMethod != null) {
            return cachedMethod;
        }
        Method foundMethod = findCompatibleMethod(interfaceMethod);
        methodCache.put(interfaceMethod, foundMethod);
        return foundMethod;
    }
}
