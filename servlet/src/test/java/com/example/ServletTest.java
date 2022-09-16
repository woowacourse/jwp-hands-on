package com.example;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ServletTest {

    @Test
    void testSharedCounter() throws Exception {
        // 톰캣 서버 시작
        final var tomcatStarter = TestHttpUtils.createTomcatStarter();
        tomcatStarter.start();

        // shared-counter 페이지를 3번 호출한다.
        final var PATH = "/shared-counter";
        TestHttpUtils.send(PATH);
        TestHttpUtils.send(PATH);
        final var response = TestHttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        // 그냥 integer를 공유하더라도 순차적으로 요청하기 때문에 오류가 발생하지는 않는다. 그러나
        // 동시에 여러 스레드에서 접근할 경우 thread inferences 문제가 발생할 수 있다.
        // 따라서 AtomicInteger를 사용하여 동기화 처리를 해주었다.
        assertThat(Integer.parseInt(response.body())).isEqualTo(3);
    }

    @Test
    void testLocalCounter() throws Exception {
        // 톰캣 서버 시작
        final var tomcatStarter = TestHttpUtils.createTomcatStarter();
        tomcatStarter.start();

        // local-counter 페이지를 3번 호출한다.
        final var PATH = "/local-counter";
        TestHttpUtils.send(PATH);
        TestHttpUtils.send(PATH);
        final var response = TestHttpUtils.send(PATH);

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // expected를 0이 아닌 올바른 값으로 바꿔보자.
        // 예상한 결과가 나왔는가? 왜 이런 결과가 나왔을까?
        // method의 local variable로 선언되기 때문에 메서드 외부에서 변수에 접근할 수 없다.
        // 따라서 메서드가 끝나면 GC의 대상이 되어 메모리가 회수되기 떄문에 local variable로 선언할 수 없다.
        assertThat(Integer.parseInt(response.body())).isEqualTo(1);
    }
}
