package com.example;

import static com.example.KoreanServlet.인코딩;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FilterTest {

    @Test
    void testFilter() throws Exception {
        // 톰캣 서버 시작
        final var tomcatStarter = TestHttpUtils.createTomcatStarter();
        tomcatStarter.start();

        final var response = TestHttpUtils.send("/korean");

        // 톰캣 서버 종료
        tomcatStarter.stop();

        assertThat(response.statusCode()).isEqualTo(200);

        // 테스트가 통과하도록 CharacterEncodingFilter 클래스를 수정해보자.
        assertThat(response.body()).isEqualTo(인코딩);
    }
}
