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
 * A {@link Retrofitter} implementation which caches method lookup.
 * This is an order of magnitude faster then {@link RetrofitterWithoutMethodLookupCaching}
 * when doing repeated calls to a retrofitted object.
 * 
 * @author Ville Peurala
 */
public class RetrofitterWithMethodLookupCaching extends AbstractRetrofitter {
    @Override
    protected AbstractMethodLookupHelper createMethodLookupHelper(Object target) {
        return new CachingMethodLookupHelper(target);
    }

    public Retrofitter withMethodLookupCaching() {
        return this;
    }

    public Retrofitter withoutMethodLookupCaching() {
        return new RetrofitterWithoutMethodLookupCaching();
    }
}
