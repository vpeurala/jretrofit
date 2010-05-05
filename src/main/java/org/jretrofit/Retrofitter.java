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
package org.jretrofit;

/**
 * Implementations of this interface create retrofitted objects.
 * 
 * @author Ville Peurala
 */
public interface Retrofitter {
    /**
     * Make the target implement a single interface with a "complete" retrofitting,
     * which means that the target must "implement" ALL methods specified on
     * the given interface.
     * 
     * @param target a target object.
     * @param interfaceToImplement an interface to "implement".
     * @return a retrofitted object which can be cast to the given interface.
     * @throws AllMethodsNotImplementedException if the target object does not wholly "implement" the required interface.
     */
    public <T> T complete(Object target, Class<T> interfaceToImplement)
            throws AllMethodsNotImplementedException;
    /**
     * Make the target implement multiple interfaces with a "complete" retrofitting,
     * which means that the target must "implement" ALL methods specified on
     * ALL of the given interfaces.
     * 
     * @param target a target object.
     * @param interfacesToImplement an array of interfaces to "implement".
     * @return a retrofitted object which can be cast to any of the given interfaces.
     * @throws AllMethodsNotImplementedException if the target object does not wholly "implement" all of the required interfaces.
     */
    public Object complete(Object target, Class<?>[] interfacesToImplement)
            throws AllMethodsNotImplementedException;
    /**
     * Make the target implement a single interface with a "partial" retrofitting,
     * which means that it is not checked whether the target has all the required
     * methods. Use this if you want to use just a subset of the methods defined
     * on the interface.
     * 
     * @param target a target object.
     * @param interfaceToImplement an interface to "implement" partially.
     * @return a retrofitted object which can be cast to the given interface.
     */
    public <T> T partial(Object target, Class<T> interfaceToImplement);
    /**
     * Make the target implement multiple interfaces with a "partial" retrofitting,
     * which means that it is not checked whether the target has all the required
     * methods. Use this if you want to use just a subset of the methods defined
     * on the interface.
     * 
     * @param target a target object.
     * @param interfacesToImplement an array of interfaces to "implement" partially.
     * @return a retrofitted object which can be cast to any of the given interfaces.
     */
    public Object partial(Object target, Class<?>[] interfacesToImplement);
    /**
     * Returns a {@link Retrofitter} which caches method lookup, which means that retrofitted
     * objects returned by it generally perform a lot faster than objects created
     * by a non-caching retrofitter. Use this if you want speed.
     * 
     * @return a caching {@link Retrofitter} implementation.
     */
    public Retrofitter withMethodLookupCaching();
    /**
     * Returns a {@link Retrofitter} which does not cache method lookup, which means that retrofitted
     * objects returned by it generally perform a lot slower than objects created by a 
     * caching retrofitter. Use this if speed is not an issue.
     * 
     * @return a non-caching {@link Retrofitter} implementation.
     */
    public Retrofitter withoutMethodLookupCaching();
}
