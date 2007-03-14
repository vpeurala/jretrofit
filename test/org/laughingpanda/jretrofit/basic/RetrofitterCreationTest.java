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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.TestCase;

import org.laughingpanda.jretrofit.Retrofit;
import org.laughingpanda.jretrofit.Retrofitter;
import org.laughingpanda.jretrofit.RetrofitterWithMethodLookupCaching;
import org.laughingpanda.jretrofit.RetrofitterWithoutMethodLookupCaching;

/**
 * @author Ville Peurala
 * @author Antti Mattila
 */
public class RetrofitterCreationTest extends TestCase {
    public void testRetrofitterWithMethodLookupCachingReturnsANewNonCachingInstanceFromMethodWithoutLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithMethodLookupCaching();
        assertNotSame(retrofitter, retrofitter.withoutMethodLookupCaching());
        assertTrue(retrofitter.withoutMethodLookupCaching() instanceof RetrofitterWithoutMethodLookupCaching);
    }

    public void testRetrofitterWithMethodLookupCachingReturnsItselfFromMethodWithMethodLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithMethodLookupCaching();
        assertSame(retrofitter, retrofitter.withMethodLookupCaching());
    }

    public void testRetrofitterWithoutMethodLookupCachingReturnsANewCachingInstanceFromMethodWithLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithoutMethodLookupCaching();
        assertNotSame(retrofitter, retrofitter.withMethodLookupCaching());
        assertTrue(retrofitter.withMethodLookupCaching() instanceof RetrofitterWithMethodLookupCaching);
    }

    public void testRetrofitterWithoutMethodLookupCachingReturnsItselfFromMethodWithoutMethodLookupCaching() {
        Retrofitter retrofitter = new RetrofitterWithoutMethodLookupCaching();
        assertSame(retrofitter, retrofitter.withoutMethodLookupCaching());
    }

    public void testStaticClassRetrofitCanCreateCachingRetrofitter() {
        Object created = Retrofit.withMethodLookupCaching();
        assertTrue(created instanceof RetrofitterWithMethodLookupCaching);
    }

    public void testStaticClassRetrofitCanCreateNonCachingRetrofitter() {
        Object created = Retrofit.withoutMethodLookupCaching();
        assertTrue(created instanceof RetrofitterWithoutMethodLookupCaching);
    }

    public void testRetrofitCanSerializeObjects() throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        oos.writeObject(Retrofit.complete(new Object(), Serializable.class));
        oos.close();
    }
}