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

/**
 * @author Ville Peurala
 */
abstract class AbstractMethodLookupHelper {
    private Object target;

    protected AbstractMethodLookupHelper(Object target) {
        this.target = target;
    }

    protected boolean areMethodsCompatible(Method requestedMethod,
            Method candidateMethod) {
        return areNamesCompatible(requestedMethod, candidateMethod)
                && areParametersCompatible(requestedMethod, candidateMethod)
                && areReturnValuesCompatible(requestedMethod, candidateMethod);
    }

    private boolean areNamesCompatible(Method requestedMethod,
            Method candidateMethod) {
        return requestedMethod.getName().equals(candidateMethod.getName());
    }

    private boolean areParametersCompatible(Method requestedMethod,
            Method candidateMethod) {
        Class[] requestedParameterTypes = requestedMethod.getParameterTypes();
        Class[] candidateParameterTypes = candidateMethod.getParameterTypes();
        if (requestedParameterTypes.length != candidateParameterTypes.length) {
            return false;
        }
        for (int i = 0; i < requestedParameterTypes.length; i++) {
            Class currentRequestedParameterType = requestedParameterTypes[i];
            Class currentCandidateParameterType = candidateParameterTypes[i];
            if (!currentCandidateParameterType
                    .isAssignableFrom(currentRequestedParameterType)) {
                return false;
            }
        }
        return true;
    }

    private boolean areReturnValuesCompatible(Method requestedMethod,
            Method candidateMethod) {
        Class requstedReturnValueType = requestedMethod.getReturnType();
        Class candidateReturnValueType = candidateMethod.getReturnType();
        return requstedReturnValueType
                .isAssignableFrom(candidateReturnValueType);
    }

    abstract Method findMethodToCall(Method interfaceMethod);

    protected final Object getTarget() {
        return target;
    }
}
