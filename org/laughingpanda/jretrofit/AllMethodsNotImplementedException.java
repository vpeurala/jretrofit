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
 * This exception is thrown if you try to use {@link Retrofit#complete(Object, Class) complete}
 * retrofitting, but your target object does not "implement" all of the required methods.
 * 
 * @author Ville Peurala
 */
public class AllMethodsNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1977L;

    private final Method[] notImplementedMethods;

    public AllMethodsNotImplementedException(Method[] notImplementedMethods) {
        this.notImplementedMethods = notImplementedMethods;
    }

    /**
     * Get the methods which were not "implemented".
     * 
     * @return an array of methods that the target object did not "implement".
     */
    public Method[] getNotImplementedMethods() {
        return notImplementedMethods;
    }

    private String notImplementedMethodsAsString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < notImplementedMethods.length; i++) {
            buffer.append(notImplementedMethods[i].getName());
            if (i < (notImplementedMethods.length - 1)) {
                buffer.append(", ");
            }
        }
        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#toString()
     */
    public String toString() {
        return "Methods not implemented: " + notImplementedMethodsAsString()
                + ".";
    }
}
