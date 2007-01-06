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
package org.laughingpanda.jretrofit.basic;

import java.lang.reflect.Constructor;

import org.laughingpanda.jretrofit.Retrofit;
import org.laughingpanda.jretrofit.Retrofitter;

/**
 * @author Ville Peurala
 */
public class RetrofitWithMethodCachingTest extends AbstractRetrofitTestCase {
    protected Retrofitter createRetrofitter() {
        return Retrofit.withMethodLookupCaching();
    }

    public void testCachedMethodCallShouldWorkInSameWayAsUncached() {
        assertEquals("Antti", human.getName());
        assertEquals("Antti", human.getName());
    }

    public void testCallPrivateConstructorForBetterTestCoverage()
            throws Exception {
        // This is a minor detail, but it's irritating
        // to have under 100% unit test coverage just 
        // because the constructor of class Retrofit is
        // private (it is a library class, never meant to
        // be instantiated). So we call it here.
        Constructor c = Retrofit.class.getDeclaredConstructor(null);
        c.setAccessible(true);
        Retrofit retrofit = (Retrofit) c.newInstance(null);
        assertNotNull(retrofit);
    }
}
