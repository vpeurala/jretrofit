package org.laughingpanda.jretrofit;

import org.laughingpanda.jretrofit.fixture.CompleteHuman;
import org.laughingpanda.jretrofit.fixture.Human;
import org.laughingpanda.jretrofit.fixture.Person;

/**
 * @author Ville Peurala
 */
public class RetrofitWithoutMethodCachingTest extends AbstractRetrofitTestCase {
    protected Retrofitter createRetrofitter() {
        return Retrofit.withoutMethodLookupCaching();
    }

    public void testCallStaticHelpersForBetterTestCoverage() {
        // Just calls some static helpers which delegate directly to other methods.
        // Those methods are tested on their own, so no need to have 
        // excessive asserts here.
        Object retrofittedWithStaticHelperAndSingleInterfacePartial = Retrofit
                .partial(new Person(), Human.class);
        assertTrue(retrofittedWithStaticHelperAndSingleInterfacePartial instanceof Human);
        Object retrofittedWithStaticHelperAndSingleInterfaceArrayPartial = Retrofit
                .partial(new Person(), new Class[] { Human.class });
        assertTrue(retrofittedWithStaticHelperAndSingleInterfaceArrayPartial instanceof Human);
        Object retrofittedWithStaticHelperAndSingleInterfaceComplete = Retrofit
                .complete(new CompleteHuman(), Human.class);
        assertTrue(retrofittedWithStaticHelperAndSingleInterfaceComplete instanceof Human);
        Object retrofittedWithStaticHelperAndSingleInterfaceArrayComplete = Retrofit
                .complete(new CompleteHuman(), new Class[] { Human.class });
        assertTrue(retrofittedWithStaticHelperAndSingleInterfaceArrayComplete instanceof Human);
    }
}
