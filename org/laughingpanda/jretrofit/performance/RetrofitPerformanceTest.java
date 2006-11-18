package org.laughingpanda.jretrofit.performance;

import junit.framework.TestCase;

import org.laughingpanda.jretrofit.Retrofit;
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
        long timeFor100000RetrofittingsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.withoutMethodLookupCaching().partial(person,
                                Human.class);
                    }
                }, 100000);
        long timeFor100000RetrofittingsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.withoutMethodLookupCaching().partial(person,
                                Human.class);
                    }
                }, 100000);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            System.out.println("timeFor100000Creations: "
                    + timeFor100000Creations);
            System.out.println("timeFor100000RetrofittingsWithoutCache: "
                    + timeFor100000RetrofittingsWithoutCache);
            System.out.println("timeFor100000RetrofittingsWithCache: "
                    + timeFor100000RetrofittingsWithCache);
        }
    }

    public void testBenchmarkDifferenceBetweenDirectCallAndCallThroughRetrofit()
            throws Exception {
        final Person person = new Person();
        final Human humanWithoutCache = (Human) Retrofit
                .withoutMethodLookupCaching().partial(person, Human.class);
        final Human humanWithCache = (Human) Retrofit.withMethodLookupCaching()
                .partial(person, Human.class);
        long timeFor100000DirectGetNameCalls = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        person.getName();
                    }
                }, 100000);
        long timeFor100000RetrofittedGetNameCallsWithoutCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        humanWithoutCache.getName();
                    }
                }, 100000);
        long timeFor100000RetrofittedGetNameCallsWithCache = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        humanWithCache.getName();
                    }
                }, 100000);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            System.out.println("timeFor100000DirectGetNameCalls: "
                    + timeFor100000DirectGetNameCalls);
            System.out
                    .println("timeFor100000RetrofittedGetNameCallsWithoutCache: "
                            + timeFor100000RetrofittedGetNameCallsWithoutCache);
            System.out
                    .println("timeFor100000RetrofittedGetNameCallsWithCache: "
                            + timeFor100000RetrofittedGetNameCallsWithCache);
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
