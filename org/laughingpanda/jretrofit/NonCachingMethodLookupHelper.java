package org.laughingpanda.jretrofit;

import java.lang.reflect.Method;

/**
 * @author Ville Peurala / Reaktor Innovations
 */
class NonCachingMethodLookupHelper extends AbstractMethodLookupHelper {
    public NonCachingMethodLookupHelper(Object target) {
        super(target);
    }

    Method findMethodToCall(Method interfaceMethod) {
        Method[] targetMethods = getTarget().getClass().getMethods();
        for (int i = 0; i < targetMethods.length; i++) {
            Method currentMethod = targetMethods[i];
            if (areMethodsCompatible(interfaceMethod, currentMethod)) {
                return currentMethod;
            }
        }
        throw new UnsupportedOperationException("Target object '" + getTarget()
                + "' does not have a method which is compatible with '"
                + interfaceMethod + "'!");
    }
}
