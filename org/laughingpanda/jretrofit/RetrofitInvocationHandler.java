package org.laughingpanda.jretrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Ville Peurala
 */
final class RetrofitInvocationHandler implements InvocationHandler {
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
