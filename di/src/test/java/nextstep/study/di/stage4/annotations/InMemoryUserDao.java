package nextstep.study.di.stage4.annotations;

import nextstep.study.User;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
class InMemoryUserDao implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserDao.class);

    private static final Map<Long, User> users = new HashMap<>();

    private final JdbcDataSource dataSource;

    public InMemoryUserDao() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");

        this.dataSource = jdbcDataSource;
    }

    public void insert(User user) {
        try (final var connection = dataSource.getConnection()) {
            users.put(user.getId(), user);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    public User findById(long id) {
        try (final var connection = dataSource.getConnection()) {
            return users.get(id);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
