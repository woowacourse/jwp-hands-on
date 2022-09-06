package concurrency.stage2;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class AppTest {

    private static final AtomicInteger successResponseCount = new AtomicInteger(0);
    private static final AtomicInteger requestTimedOutCount = new AtomicInteger(0);

    /**
     * 1. App 클래스의 애플리케이션을 실행시켜 서버를 띄운다.
     * 2. 아래 테스트를 실행시킨다.
     * 3. AppTest가 아닌 App의 콘솔에서 SampleController가 생성한 http call count 로그를 확인한다.
     * 4. application.yml에서 설정값을 변경해보면서 어떤 차이점이 있는지 분석해본다.
     * - 로그가 찍힌 시간
     * - 스레드명(nio-8080-exec-x)으로 생성된 스레드 갯수를 파악
     * - http call count
     * - 테스트 결과값
     */
    @Test
    void test() throws Exception {
        final var NUMBER_OF_THREAD = 10;
        var threads = new Thread[NUMBER_OF_THREAD];

        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            threads[i] = new Thread(() -> incrementIfOk(TestHttpUtils.send("/test")));
        }

        for (final var thread : threads) {
            thread.start();
             Thread.sleep(50);
        }

        for (final var thread : threads) {
            thread.join(); // 처리 완료될 때까지 평생 대기.
        }

        assertThat(successResponseCount.intValue()).isEqualTo(2);
    }

    private static void incrementIfOk(final HttpResponse<String> response) {
        if (response == null) {
            System.out.println("request timed out: " + requestTimedOutCount.incrementAndGet());
            return;
        }
        if (response.statusCode() == 200) {
            successResponseCount.incrementAndGet();
        }
    }
}
