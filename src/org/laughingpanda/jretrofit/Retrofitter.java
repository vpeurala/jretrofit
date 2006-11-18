package org.laughingpanda.jretrofit;

/**
 * @author Ville Peurala
 */
public interface Retrofitter {
    public Retrofitter withMethodLookupCaching();
    public Retrofitter withoutMethodLookupCaching();
    public Object partial(Object target, Class interfaceToImplement);
    public Object partial(Object target, Class[] interfacesToImplement);
    //public Object complete(Object target, Class interfaceToImplement);
    //public Object complete(Object target, Class[] interfacesToImplement);
}
