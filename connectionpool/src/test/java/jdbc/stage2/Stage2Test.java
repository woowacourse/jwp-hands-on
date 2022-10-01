package jdbc.stage2;

import static com.zaxxer.hikari.util.UtilityElf.quietlySleep;
import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import java.lang.reflect.Field;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Stage2Test {

    private static final Logger log = LoggerFactory.getLogger(Stage2Test.class);

    /**
     * spring boot에서 설정 파일인 application.yml를 사용하여 DataSource를 설정할 수 있다.
     * 하지만 DataSource를 여러 개 사용하거나 세부 설정을 하려면 빈을 직접 생성하는 방법을 사용한다.
     * DataSourceConfig 클래스를 찾아서 어떻게 빈으로 직접 생성하는지 확인해보자.
     * 그리고 아래 DataSource가 직접 생성한 빈으로 주입 받았는지 getPoolName() 메서드로 확인해보자.
     */
    @Autowired
    private DataSource dataSource;

    @Test
    void test() throws InterruptedException {
        final var hikariDataSource = (HikariDataSource) dataSource;
        final var hikariPool = getPool((HikariDataSource) dataSource);

        // 설정한 커넥션 풀 최대값보다 더 많은 스레드를 생성해서 동시에 디비에 접근을 시도하면 어떻게 될까?
        final var threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(getConnection());
        }

        for (final var thread : threads) {
            thread.start();
        }

        for (final var thread : threads) {
            thread.join();
        }

        // 동시에 많은 요청이 몰려도 최대 풀 사이즈를 유지한다.
        assertThat(hikariPool.getTotalConnections()).isEqualTo(5);

        // DataSourceConfig 클래스에서 직접 생성한 커넥션 풀.
        assertThat(hikariDataSource.getPoolName()).isEqualTo("gugu");
    }

    // 데이터베이스에 연결만 하는 메서드. 커넥션 풀에 몇 개의 연결이 생기는지 확인하는 용도.
    private Runnable getConnection() {
        return () -> {
            try {
                log.info("Before acquire ");
                try (Connection ignored = dataSource.getConnection()) {
                    log.info("After acquire ");
                    quietlySleep(500); // Thread.sleep(500)과 동일한 기능
                }
            } catch (Exception e) {
            }
        };
    }

    // 학습 테스트를 위해 HikariPool을 추출
    public static HikariPool getPool(final HikariDataSource hikariDataSource)
    {
        try {
            Field field = hikariDataSource.getClass().getDeclaredField("pool");
            field.setAccessible(true);
            return (HikariPool) field.get(hikariDataSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
