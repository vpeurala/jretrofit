package org.laughingpanda.jretrofit.examples;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementExample {
    public boolean executePreparedStatement(PreparedStatement statement)
            throws SQLException {
        return statement.execute();
    }
}
