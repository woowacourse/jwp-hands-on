package transaction.stage1.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCreator {
    PreparedStatement createPreparedStatement(final Connection con) throws SQLException;
}
