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
package org.laughingpanda.jretrofit.performance;

import junit.framework.TestCase;

import org.laughingpanda.jretrofit.Retrofit;
import org.laughingpanda.jretrofit.fixture.CompleteHuman;
import org.laughingpanda.jretrofit.fixture.Human;
import org.laughingpanda.jretrofit.fixture.Person;

/**
 * Some crude performance tests, mainly to see the difference
 * between looking up the method via reflection on every call
 * vs. caching the method lookups in a HashMap. Some 
 * very non-scientific performance measurements:
 * 
 * Without caching:
 * ------------------------------------------
 * timeFor100000Creations: 10
 * timeFor100000Retrofittings: 3164
 * timeFor100000DirectGetNameCalls: 10
 * timeFor100000RetrofittedGetNameCalls: 6320
 * 
 * With caching:
 * ------------------------------------------
 * timeFor100000Creations: 20
 * timeFor100000Retrofittings: 3035
 * timeFor100000DirectGetNameCalls: 0
 * timeFor100000RetrofittedGetNameCalls: 290
 * 
 * It looks like it is about 30 times faster with method
 * lookup caching on my machine. Your mileage may vary.
 * 
 * @author Ville Peurala
 */
public class RetrofitPerformanceTest extends TestCase {
    /**
     * Set this to true if you want to record performance results.
     */
    public static final boolean OUTPUT_RESULTS_TO_SYSOUT = true;

    public void testBenchmarkDifferenceBetweenOrdinaryCreationAndRetrofitting()
            throws Exception {
        long timeFor100000Creations = executeTimedOperation(new Operation() {
            public void execute() {
                new Person();
            }
        }, 100000);
        long timeFor100000PartialRetrofittingsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.withoutMethodLookupCaching().partial(person,
                                Human.class);
                    }
                }, 100000);
        long timeFor100000PartialRetrofittingsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.withoutMethodLookupCaching().partial(person,
                                Human.class);
                    }
                }, 100000);
        long timeFor1000CompleteRetrofittingsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        CompleteHuman completeHuman = new CompleteHuman();
                        Retrofit.withoutMethodLookupCaching().complete(
                                completeHuman, Human.class);
                    }
                }, 1000);
        long timeFor1000CompleteRetrofittingsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        CompleteHuman completeHuman = new CompleteHuman();
                        Retrofit.withMethodLookupCaching().complete(
                                completeHuman, Human.class);
                    }
                }, 1000);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            System.out.println("timeFor100000Creations: "
                    + timeFor100000Creations);
            System.out
                    .println("timeFor100000PartialRetrofittingsWithoutCache: "
                            + timeFor100000PartialRetrofittingsWithoutCache);
            System.out.println("timeFor100000PartialRetrofittingsWithCache: "
                    + timeFor100000PartialRetrofittingsWithCache);
            System.out.println("timeFor1000CompleteRetrofittingsWithoutCache: "
                    + timeFor1000CompleteRetrofittingsWithoutCache);
            System.out.println("timeFor1000CompleteRetrofittingsWithCache: "
                    + timeFor1000CompleteRetrofittingsWithCache);
        }
    }

    public void testBenchmarkDifferenceBetweenDirectCallAndCallThroughRetrofit()
            throws Exception {
        final Person person = new Person();
        final CompleteHuman completeHuman = new CompleteHuman();
        final Human partialHumanWithoutCache = Retrofit
                .withoutMethodLookupCaching().partial(person, Human.class);
        final Human partialHumanWithCache = Retrofit
                .withMethodLookupCaching().partial(person, Human.class);
        final Human completeHumanWithoutCache = Retrofit
                .withoutMethodLookupCaching().complete(completeHuman,
                        Human.class);
        final Human completeHumanWithCache = Retrofit.withMethodLookupCaching()
                .complete(completeHuman, Human.class);
        long timeFor100000DirectGetNameCalls = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        person.getName();
                    }
                }, 100000);
        long timeFor100000PartiallyRetrofittedGetNameCallsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        partialHumanWithoutCache.getName();
                    }
                }, 100000);
        long timeFor100000PartiallyRetrofittedGetNameCallsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        partialHumanWithCache.getName();
                    }
                }, 100000);
        long timeFor100000CompletelyRetrofittedGetNameCallsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        completeHumanWithoutCache.getName();
                    }
                }, 100000);
        long timeFor100000CompletelyRetrofittedGetNameCallsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        completeHumanWithCache.getName();
                    }
                }, 100000);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            System.out.println("timeFor100000DirectGetNameCalls: "
                    + timeFor100000DirectGetNameCalls);
            System.out
                    .println("timeFor100000PartiallyRetrofittedGetNameCallsWithoutCache: "
                            + timeFor100000PartiallyRetrofittedGetNameCallsWithoutCache);
            System.out
                    .println("timeFor100000PartiallyRetrofittedGetNameCallsWithCache: "
                            + timeFor100000PartiallyRetrofittedGetNameCallsWithCache);
            System.out
                    .println("timeFor100000CompletelyRetrofittedGetNameCallsWithoutCache: "
                            + timeFor100000CompletelyRetrofittedGetNameCallsWithoutCache);
            System.out
                    .println("timeFor100000CompletelyRetrofittedGetNameCallsWithCache: "
                            + timeFor100000CompletelyRetrofittedGetNameCallsWithCache);
        }
    }
    private long executeTimedOperation(Operation operation, int times) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            operation.execute();
        }
        long end = System.currentTimeMillis();
        return end - begin;
    }

    private static interface Operation {
        void execute();
    }
}
