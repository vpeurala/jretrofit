package org.laughingpanda.jretrofit;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Ville Peurala
 */
public class AllMethodsNotImplementedExceptionTest extends TestCase {
    public void testToStringWithASingleUnimplementedMethod() throws Exception {
        Method[] methods = new Method[] { Object.class.getMethod("toString",
                null) };
        AllMethodsNotImplementedException exception = new AllMethodsNotImplementedException(
                methods);
        assertEquals("Methods not implemented: toString.", exception.toString());
    }

    public void testToStringWithTwoUnimplementedMethods() throws Exception {
        Method[] methods = new Method[] {
                Object.class.getMethod("toString", null),
                Object.class.getMethod("equals", new Class[] { Object.class }) };
        AllMethodsNotImplementedException exception = new AllMethodsNotImplementedException(
                methods);
        assertEquals("Methods not implemented: toString, equals.", exception
                .toString());
    }

    public void testToStringWithThreeUnimplementedMethods() throws Exception {
        Method[] methods = new Method[] {
                Object.class.getMethod("toString", null),
                Object.class.getMethod("equals", new Class[] { Object.class }),
                Object.class.getMethod("hashCode", null) };
        AllMethodsNotImplementedException exception = new AllMethodsNotImplementedException(
                methods);
        assertEquals("Methods not implemented: toString, equals, hashCode.",
                exception.toString());
    }
}
