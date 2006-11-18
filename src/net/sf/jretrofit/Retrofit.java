package net.sf.jretrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ville Peurala
 */
public final class Retrofit {

    /**
     * TODO: write javadoc
     * @param target
     * @param interfaceToImplement
     * @return
     */
    public static Object retrofit(Object target, Class interfaceToImplement) {
        return retrofit(target, interfaceToImplement, true);
    }

    /**
     * TODO: write javadoc
     * @param target
     * @param interfaceToImplement
     * @return
     */
    public static Object retrofit(Object target, Class interfaceToImplement,
            boolean cacheMethodLookups) {
        return retrofit(target, new Class[] { interfaceToImplement },
                cacheMethodLookups);
    }

    /**
     * TODO: write javadoc
     * @param target
     * @param interfacesToImplement
     * @return
     */
    public static Object retrofit(Object target, Class[] interfacesToImplement) {
        return retrofit(target, interfacesToImplement, true);
    }

    /**
     * TODO: write javadoc
     * @param target
     * @param interfacesToImplement
     * @return
     */
    public static Object retrofit(Object target, Class[] interfacesToImplement,
            boolean cacheMethodLookups) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                allInterfacesToImplement(target, interfacesToImplement),
                new RetrofitInvocationHandler(target, cacheMethodLookups));
    }

    private static Class[] allInterfacesToImplement(Object target,
            Class[] interfacesToImplement) {
        ArrayList allInterfacesToImplement = new ArrayList();
        allInterfacesToImplement.addAll(Arrays.asList(interfacesToImplement));
        allInterfacesToImplement.addAll(Arrays.asList(target.getClass()
                .getInterfaces()));
        return (Class[]) allInterfacesToImplement
                .toArray(new Class[allInterfacesToImplement.size()]);
    }

    private Retrofit() {
    // Cannot be instantiated.
    }

    private static final class RetrofitInvocationHandler implements
            InvocationHandler {
        private final Object target;
        private final boolean cacheMethodLookups;
        private final Map methodCache;

        public RetrofitInvocationHandler(Object target,
                boolean cacheMethodLookups) {
            this.target = target;
            this.cacheMethodLookups = cacheMethodLookups;
            if (cacheMethodLookups) {
                methodCache = new HashMap();
            } else {
                methodCache = null;
            }
        }

        private Method findMethodToCall(Method interfaceMethod) {
            if (cacheMethodLookups) {
                Method cachedMethod = (Method) methodCache.get(interfaceMethod);
                if (cachedMethod != null) {
                    return cachedMethod;
                }
            }
            Method[] targetMethods = target.getClass().getMethods();
            for (int i = 0; i < targetMethods.length; i++) {
                Method currentMethod = targetMethods[i];
                if (areMethodsCompatible(interfaceMethod, currentMethod)) {
                    if (cacheMethodLookups) {
                        methodCache.put(interfaceMethod, currentMethod);
                    }
                    return currentMethod;
                }
            }
            throw new UnsupportedOperationException("Target object '" + target
                    + "' does not have a method which is compatible with '"
                    + interfaceMethod + "'!");
        }

        private boolean areMethodsCompatible(Method requestedMethod,
                Method candidateMethod) {
            return areNamesCompatible(requestedMethod, candidateMethod)
                    && areParametersCompatible(requestedMethod, candidateMethod)
                    && areReturnValuesCompatible(requestedMethod,
                            candidateMethod);
        }

        private boolean areNamesCompatible(Method requestedMethod,
                Method candidateMethod) {
            return requestedMethod.getName().equals(candidateMethod.getName());
        }

        private boolean areParametersCompatible(Method requestedMethod,
                Method candidateMethod) {
            Class[] requestedParameterTypes = requestedMethod
                    .getParameterTypes();
            Class[] candidateParameterTypes = candidateMethod
                    .getParameterTypes();
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

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            Method targetMethod = findMethodToCall(method);
            try {
                return targetMethod.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
