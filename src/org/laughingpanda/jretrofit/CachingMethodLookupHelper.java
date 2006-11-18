package org.laughingpanda.jretrofit;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Ville Peurala
 */
class CachingMethodLookupHelper extends AbstractMethodLookupHelper {
    private final HashMap methodCache = new HashMap();

    protected CachingMethodLookupHelper(Object target) {
        super(target);
    }

    Method findMethodToCall(Method interfaceMethod) {
        Method cachedMethod = (Method) methodCache.get(interfaceMethod);
        if (cachedMethod != null) {
            return cachedMethod;
        }
        Method[] targetMethods = getTarget().getClass().getMethods();
        for (int i = 0; i < targetMethods.length; i++) {
            Method currentMethod = targetMethods[i];
            if (areMethodsCompatible(interfaceMethod, currentMethod)) {
                methodCache.put(interfaceMethod, currentMethod);
                return currentMethod;
            }
        }
        throw new UnsupportedOperationException("Target object '" + getTarget()
                + "' does not have a method which is compatible with '"
                + interfaceMethod + "'!");
    }
}
