package net.sf.jretrofit;

import net.sf.jretrofit.fixture.Human;

/**
 * @author Ville Peurala
 */
public class RetrofitWithoutMethodCachingTest extends AbstractRetrofitTestCase {
    protected Human createHuman() {
        return (Human) Retrofit.retrofit(person, Human.class, false);
    }
}
