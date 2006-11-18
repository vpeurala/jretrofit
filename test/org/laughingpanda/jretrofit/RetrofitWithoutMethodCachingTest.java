package org.laughingpanda.jretrofit;

import org.laughingpanda.jretrofit.fixture.Human;

/**
 * @author Ville Peurala
 */
public class RetrofitWithoutMethodCachingTest extends AbstractRetrofitTestCase {
    protected Human createHuman() {
        return (Human) Retrofit.withoutMethodLookupCaching().partial(person,
                Human.class);
    }

    public void testCallStaticHelpersForBetterTestCoverage() {
        // Just calls some static helpers which delegate directly to other methods.
        Object retrofittedWithStaticHelperAndSingleInterface = Retrofit
                .partial(person, Human.class);
        assertTrue(retrofittedWithStaticHelperAndSingleInterface instanceof Human);
        Object retrofittedWithStaticHelperAndSingleInterfaceArray = Retrofit
                .partial(person, new Class[] { Human.class });
        assertTrue(retrofittedWithStaticHelperAndSingleInterfaceArray instanceof Human);
    }
}
