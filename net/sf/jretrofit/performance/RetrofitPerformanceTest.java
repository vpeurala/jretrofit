package net.sf.jretrofit.performance;

import junit.framework.TestCase;
import net.sf.jretrofit.Retrofit;
import net.sf.jretrofit.fixture.Human;
import net.sf.jretrofit.fixture.Person;

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
    public static final boolean OUTPUT_RESULTS_TO_SYSOUT = false;

    public void testBenchmarkDifferenceBetweenOrdinaryCreationAndRetrofitting()
            throws Exception {
        long timeFor100000Creations = executeTimedOperation(new Operation() {
            public void execute() {
                new Person();
            }
        }, 100000);
        long timeFor100000Retrofittings = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        Person person = new Person();
                        Retrofit.retrofit(person, Human.class, true);
                    }
                }, 100000);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            System.out.println("timeFor100000Creations: "
                    + timeFor100000Creations);
            System.out.println("timeFor100000Retrofittings: "
                    + timeFor100000Retrofittings);
        }
    }

    public void testBenchmarkDifferenceBetweenDirectCallAndCallThroughRetrofit()
            throws Exception {
        final Person person = new Person();
        final Human human = (Human) Retrofit
                .retrofit(person, Human.class, true);
        long timeFor100000DirectGetNameCalls = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        person.getName();
                    }
                }, 100000);
        long timeFor100000RetrofittedGetNameCalls = executeTimedOperation(
                new Operation() {
                    public void execute() {
                        human.getName();
                    }
                }, 100000);
        if (OUTPUT_RESULTS_TO_SYSOUT) {
            System.out.println("timeFor100000DirectGetNameCalls: "
                    + timeFor100000DirectGetNameCalls);
            System.out.println("timeFor100000RetrofittedGetNameCalls: "
                    + timeFor100000RetrofittedGetNameCalls);
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
