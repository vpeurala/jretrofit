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
package org.laughingpanda.jretrofit;

/**
 * A {@link Retrofitter} implementation which does not cache method lookup.
 * This is quite slow, as every method call will go through a lot of reflection
 * to deduce the right method to call. 
 * 
 * @author Ville Peurala
 */
public class RetrofitterWithoutMethodLookupCaching extends AbstractRetrofitter {
    protected AbstractMethodLookupHelper createMethodLookupHelper(Object target) {
        return new NonCachingMethodLookupHelper(target);
    }

    public final Retrofitter withMethodLookupCaching() {
        return new RetrofitterWithMethodLookupCaching();
    }

    public final Retrofitter withoutMethodLookupCaching() {
        return this;
    }
}
