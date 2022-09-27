package jdbc.stage0;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class Stage0Test {

    private static final String H2_URL = "jdbc:h2:./test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * DriverManager
     * JDBC 드라이버를 관리하는 가장 기본적인 방법.
     * 커넥션 풀, 분산 트랜잭션을 지원하지 않아서 잘 사용하지 않는다.
     *
     * JDBC 4.0 이전에는 Class.forName 메서드를 사용하여 JDBC 드라이버를 직접 등록해야 했다.
     * JDBC 4.0 부터 DriverManager가 적절한 JDBC 드라이버를 찾는다.
     *
     * Autoloading of JDBC drivers
     * https://docs.oracle.com/javadb/10.8.3.0/ref/rrefjdbc4_0summary.html
     */
    @Test
    void driverManager() throws SQLException {
        // Class.forName("org.h2.Driver"); // JDBC 4.0 부터 생략 가능
        // DriverManager 클래스를 활용하여 static 변수의 정보를 활용하여 h2 db에 연결한다.
        try (final Connection connection = null) {
            assertThat(connection.isValid(1)).isTrue();
        }
    }

    /**
     * DataSource
     * 데이터베이스, 파일 같은 물리적 데이터 소스에 연결할 때 사용하는 인터페이스.
     * 구현체는 각 vendor에서 제공한다.
     * 테스트 코드의 JdbcDataSource 클래스는 h2에서 제공하는 클래스다.
     *
     * DirverManager가 아닌 DataSource를 사용하는 이유
     * - 애플리케이션 코드를 직접 수정하지 않고 properties로 디비 연결을 변경할 수 있다.
     * - 커넥션 풀링(Connection pooling) 또는 분산 트랜잭션은 DataSource를 통해서 사용 가능하다.
     *
     * Using a DataSource Object to Make a Connection
     * https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/javax/sql/package-summary.html
     */
    @Test
    void dataSource() throws SQLException {
        final JdbcDataSource dataSource = null;

        try (final var connection = dataSource.getConnection()) {
            assertThat(connection.isValid(1)).isTrue();
        }
    }
}
