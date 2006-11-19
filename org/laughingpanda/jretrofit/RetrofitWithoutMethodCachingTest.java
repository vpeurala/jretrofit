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
