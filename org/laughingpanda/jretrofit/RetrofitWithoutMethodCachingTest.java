package org.laughingpanda.jretrofit;

import org.laughingpanda.jretrofit.Retrofit;
import org.laughingpanda.jretrofit.fixture.Human;


/**
 * @author Ville Peurala
 */
public class RetrofitWithoutMethodCachingTest extends AbstractRetrofitTestCase {
    protected Human createHuman() {
        return (Human) Retrofit.retrofit(person, Human.class, false);
    }
}
