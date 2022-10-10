package transaction.stage1.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {
    void setParameters(final PreparedStatement pstmt) throws SQLException;
}
