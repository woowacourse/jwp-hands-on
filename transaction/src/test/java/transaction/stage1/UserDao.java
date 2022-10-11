package transaction.stage1;

import transaction.stage1.jdbc.JdbcTemplate;
import transaction.stage1.jdbc.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class UserDao {

    // spring jdbc가 아닌 직접 구현한 JdbcTemplate을 사용한다.
    private final transaction.stage1.jdbc.JdbcTemplate jdbcTemplate;

    public UserDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(final Connection connection, final User user) {
        final var sql = "insert into users (account, password, email) values (?, ?, ?)";
        jdbcTemplate.update(connection, sql, user.getAccount(), user.getPassword(), user.getEmail());
    }

    public void update(final Connection connection, final User user) {
        final var sql = "update users set account = ?, password = ?, email = ? where id = ?";
        jdbcTemplate.update(connection, sql, user.getAccount(), user.getPassword(), user.getEmail(), user.getId());
    }

    public void updatePasswordGreaterThan(final Connection connection, final String password, final long id) {
        final var sql = "update users set password = ? where id >= ?";
        jdbcTemplate.update(connection, sql, password, id);
    }

    public User findById(final Connection connection, final Long id) {
        final var sql = "select id, account, password, email from users where id = ?";
        return jdbcTemplate.queryForObject(connection, sql, createRowMapper(), id);
    }

    public User findByAccount(final Connection connection, final String account) {
        final var sql = "select id, account, password, email from users where account = ?";
        return jdbcTemplate.queryForObject(connection, sql, createRowMapper(), account);
    }

    public List<User> findGreaterThan(final Connection connection, final long id) {
        final var sql = "select id, account, password, email from users where id >= ?";
        return jdbcTemplate.query(connection, sql, createRowMapper(), id);
    }

    public List<User> findByAccountGreaterThan(final Connection connection, final String account) {
        final var sql = "select id, account, password, email from users where account >= ?";
        return jdbcTemplate.query(connection, sql, createRowMapper(), account);
    }

    public List<User> findAll(final Connection connection) {
        final var sql = "select id, account, password, email from users";
        return jdbcTemplate.query(connection, sql, createRowMapper());
    }

    private static RowMapper<User> createRowMapper() {
        return (final var rs) -> new User(
                rs.getLong("id"),
                rs.getString("account"),
                rs.getString("password"),
                rs.getString("email"));
    }
}
