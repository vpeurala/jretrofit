package org.laughingpanda.jretrofit;

import java.lang.reflect.Method;

/**
 * @author Ville Peurala
 */
public class AllMethodsNotImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1977L;

    private final Method[] notImplementedMethods;

    public AllMethodsNotImplementedException(Method[] notImplementedMethods) {
        this.notImplementedMethods = notImplementedMethods;
    }

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

    public String toString() {
        return "Methods not implemented: " + notImplementedMethodsAsString()
                + ".";
    }
}
