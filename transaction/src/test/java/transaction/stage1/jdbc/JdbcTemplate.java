package transaction.stage1.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(final Connection connection, final String sql, final PreparedStatementSetter pss) throws DataAccessException {
        try (final var pstmt = connection.prepareStatement(sql)) {
            pss.setParameters(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(final Connection connection, final String sql, final Object... parameters) {
        update(connection, sql, createPreparedStatementSetter(parameters));
    }

    public void update(final String sql, final PreparedStatementSetter pss) throws DataAccessException {
        try (final var conn = dataSource.getConnection();
             final var pstmt = conn.prepareStatement(sql)) {
            pss.setParameters(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(final String sql, final Object... parameters) {
        update(sql, createPreparedStatementSetter(parameters));
    }

    public void update(final PreparedStatementCreator psc, final KeyHolder holder) {
        try (final var conn = dataSource.getConnection();
             final var ps = psc.createPreparedStatement(conn)) {
            ps.executeUpdate();

            final var rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long generatedKey = rs.getLong(1);
                log.debug("Generated Key : {}", generatedKey);
                holder.setId(generatedKey);
            }
            rs.close();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(final Connection connection, final String sql, final RowMapper<T> rm, final PreparedStatementSetter pss) {
        final var list = query(connection, sql, rm, pss);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public <T> T queryForObject(final Connection connection, final String sql, final RowMapper<T> rm, final Object... parameters) {
        return queryForObject(connection, sql, rm, createPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(final Connection connection, final String sql, final RowMapper<T> rm, final PreparedStatementSetter pss) throws DataAccessException {
        try (final var pstmt = connection.prepareStatement(sql)) {
            pss.setParameters(pstmt);
            return mapResultSetToObject(rm, pstmt);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> List<T> mapResultSetToObject(final RowMapper<T> rm, final PreparedStatement pstmt) {
        try(final var rs = pstmt.executeQuery()) {
            final var list = new ArrayList<T>();
            while (rs.next()) {
                list.add(rm.mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(final Connection connection, final String sql, final RowMapper<T> rm, final Object... parameters) {
        return query(connection, sql, rm, createPreparedStatementSetter(parameters));
    }

    private PreparedStatementSetter createPreparedStatementSetter(final Object... parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        };
    }
}
