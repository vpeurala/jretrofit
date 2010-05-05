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
    private static final int NUMBER_OF_CALLS = 100000;
    private static final int NUMBER_OF_CREATIONS = 1000;
    /**
     * Set this to true if you want to record performance results.
     */
    public static final boolean OUTPUT_RESULTS_TO_SYSOUT = true;

    public void testBenchmarkDifferenceBetweenOrdinaryCreationAndRetrofitting()
            throws Exception {
        long timeForXCreations = executeTimedOperation(new Operation() {
            public void execute() {
                new Person();
            }
        }, NUMBER_OF_CREATIONS);
        long timeForXPartialRetrofittingsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.withoutMethodLookupCaching().partial(person,
                                Human.class);
                    }
                }, NUMBER_OF_CREATIONS);
        long timeForXPartialRetrofittingsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.withoutMethodLookupCaching().partial(person,
                                Human.class);
                    }
                }, NUMBER_OF_CREATIONS);
        long timeForXCompleteRetrofittingsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        CompleteHuman completeHuman = new CompleteHuman();
                        Retrofit.withoutMethodLookupCaching().complete(
                                completeHuman, Human.class);
                    }
                }, NUMBER_OF_CREATIONS);
        long timeForXCompleteRetrofittingsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        CompleteHuman completeHuman = new CompleteHuman();
                        Retrofit.withMethodLookupCaching().complete(
                                completeHuman, Human.class);
                    }
                }, NUMBER_OF_CREATIONS);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            outputCreationResults(timeForXCreations,
                    timeForXPartialRetrofittingsWithoutCache,
                    timeForXPartialRetrofittingsWithCache,
                    timeForXCompleteRetrofittingsWithoutCache,
                    timeForXCompleteRetrofittingsWithCache);
        }
    }

    public void testBenchmarkDifferenceBetweenDirectCallAndCallThroughRetrofit()
            throws Exception {
        final Person person = new Person();
        final CompleteHuman completeHuman = new CompleteHuman();
        final Human partialHumanWithoutCache = Retrofit
                .withoutMethodLookupCaching().partial(person, Human.class);
        final Human partialHumanWithCache = Retrofit.withMethodLookupCaching()
                .partial(person, Human.class);
        final Human completeHumanWithoutCache = Retrofit
                .withoutMethodLookupCaching().complete(completeHuman,
                        Human.class);
        final Human completeHumanWithCache = Retrofit.withMethodLookupCaching()
                .complete(completeHuman, Human.class);
        long timeForXDirectGetNameCalls = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        person.getName();
                    }
                }, NUMBER_OF_CALLS);
        long timeForXPartiallyRetrofittedGetNameCallsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        partialHumanWithoutCache.getName();
                    }
                }, NUMBER_OF_CALLS);
        long timeForXPartiallyRetrofittedGetNameCallsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        partialHumanWithCache.getName();
                    }
                }, NUMBER_OF_CALLS);
        long timeForXCompletelyRetrofittedGetNameCallsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        completeHumanWithoutCache.getName();
                    }
                }, NUMBER_OF_CALLS);
        long timeForXCompletelyRetrofittedGetNameCallsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        completeHumanWithCache.getName();
                    }
                }, NUMBER_OF_CALLS);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            outputCallResults(timeForXDirectGetNameCalls,
                    timeForXPartiallyRetrofittedGetNameCallsWithoutCache,
                    timeForXPartiallyRetrofittedGetNameCallsWithCache,
                    timeForXCompletelyRetrofittedGetNameCallsWithoutCache,
                    timeForXCompletelyRetrofittedGetNameCallsWithCache);
        }
    }

    private void outputCallResults(long timeForXDirectGetNameCalls,
            long timeForXPartiallyRetrofittedGetNameCallsWithoutCache,
            long timeForXPartiallyRetrofittedGetNameCallsWithCache,
            long timeForXCompletelyRetrofittedGetNameCallsWithoutCache,
            long timeForXCompletelyRetrofittedGetNameCallsWithCache) {
        System.out.println("Time for " + NUMBER_OF_CALLS
                + " direct getName() calls: " + timeForXDirectGetNameCalls);
        System.out.println("Time for " + NUMBER_OF_CALLS
                + " partially retrofitted getName() calls without cache: "
                + timeForXPartiallyRetrofittedGetNameCallsWithoutCache);
        System.out.println("Time for " + NUMBER_OF_CALLS
                + " partially retrofitted getName() calls with cache: "
                + timeForXPartiallyRetrofittedGetNameCallsWithCache);
        System.out.println("Time for " + NUMBER_OF_CALLS
                + " completely retrofitted getName() calls without cache: "
                + timeForXCompletelyRetrofittedGetNameCallsWithoutCache);
        System.out.println("Time for " + NUMBER_OF_CALLS
                + " completely retrofitted getName() calls with cache: "
                + timeForXCompletelyRetrofittedGetNameCallsWithCache);
    }

    private void outputCreationResults(long timeForXCreations,
            long timeForXPartialRetrofittingsWithoutCache,
            long timeForXPartialRetrofittingsWithCache,
            long timeForXCompleteRetrofittingsWithoutCache,
            long timeForXCompleteRetrofittingsWithCache) {
        System.out.println("Time for " + NUMBER_OF_CREATIONS + " creations: "
                + timeForXCreations);
        System.out.println("Time for " + NUMBER_OF_CREATIONS
                + " partial retrofittings without cache: "
                + timeForXPartialRetrofittingsWithoutCache);
        System.out.println("Time for " + NUMBER_OF_CREATIONS
                + " partial retrofittings with cache: "
                + timeForXPartialRetrofittingsWithCache);
        System.out.println("Time for " + NUMBER_OF_CREATIONS
                + " complete retrofittings without cache: "
                + timeForXCompleteRetrofittingsWithoutCache);
        System.out.println("Time for " + NUMBER_OF_CREATIONS
                + " complete retrofittings with cache: "
                + timeForXCompleteRetrofittingsWithCache);
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
