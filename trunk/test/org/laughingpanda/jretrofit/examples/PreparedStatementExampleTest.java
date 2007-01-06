package org.laughingpanda.jretrofit.examples;

import java.sql.PreparedStatement;
import junit.framework.TestCase;
import org.laughingpanda.jretrofit.Retrofit;

public class PreparedStatementExampleTest extends TestCase {
    public void testExecutePreparedStatementReturnsTrueOnSuccess()
            throws Exception {
        PreparedStatementStub stub = new PreparedStatementStub();
        PreparedStatement statement = (PreparedStatement) Retrofit.partial(
                stub, PreparedStatement.class);
        assertTrue(new PreparedStatementExample()
                .executePreparedStatement(statement));
    }
}
