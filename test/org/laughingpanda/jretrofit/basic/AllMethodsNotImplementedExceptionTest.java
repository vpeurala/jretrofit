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
package org.laughingpanda.jretrofit.basic;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.laughingpanda.jretrofit.AllMethodsNotImplementedException;

/**
 * @author Ville Peurala
 */
public class AllMethodsNotImplementedExceptionTest extends TestCase {
    public void testToStringWithASingleUnimplementedMethod() throws Exception {
        Method[] methods = new Method[] { Object.class.getMethod("toString",
                (Class<?>[]) null) };
        AllMethodsNotImplementedException exception = new AllMethodsNotImplementedException(
                methods);
        assertEquals("Methods not implemented: toString.", exception.toString());
    }
    public void testToStringWithTwoUnimplementedMethods() throws Exception {
        Method[] methods = new Method[] {
                Object.class.getMethod("toString", (Class<?>[]) null),
                Object.class.getMethod("equals", new Class[] { Object.class }) };
        AllMethodsNotImplementedException exception = new AllMethodsNotImplementedException(
                methods);
        assertEquals("Methods not implemented: toString, equals.", exception
                .toString());
    }

    public void testToStringWithThreeUnimplementedMethods() throws Exception {
        Method[] methods = new Method[] {
                Object.class.getMethod("toString", (Class<?>[]) null),
                Object.class.getMethod("equals", new Class[] { Object.class }),
                Object.class.getMethod("hashCode", (Class<?>[]) null) };
        AllMethodsNotImplementedException exception = new AllMethodsNotImplementedException(
                methods);
        assertEquals("Methods not implemented: toString, equals, hashCode.",
                exception.toString());
    }
}
