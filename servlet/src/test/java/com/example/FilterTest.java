package com.example;

import org.junit.jupiter.api.Test;

import static com.example.KoreanServlet.인코딩;
import static org.assertj.core.api.Assertions.assertThat;

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
        // -> ServletResponse의 기본 인코딩은 ISO 8859-1 로 한글 지원이 안됨.
        // 따라서 유니코드인 UTF-8을 적용해서 한글인코딩을 하도록 변경!
        assertThat(response.body()).isEqualTo(인코딩);
    }
}
