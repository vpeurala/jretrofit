package org.laughingpanda.jretrofit;

/**
 * @author Ville Peurala
 */
public interface Retrofitter {
    public Object complete(Object target, Class interfaceToImplement)
            throws AllMethodsNotImplementedException;
    public Object complete(Object target, Class[] interfacesToImplement)
            throws AllMethodsNotImplementedException;
    public Object partial(Object target, Class interfaceToImplement);
    public Object partial(Object target, Class[] interfacesToImplement);
    public Retrofitter withMethodLookupCaching();
    public Retrofitter withoutMethodLookupCaching();
}
