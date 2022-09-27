package nextstep.study.di.stage1.constructorinjection;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage1Test {

    /**
     * 0단계의 주요 문제는 정적 메서드만 있어서 클래스 간에 매우 긴밀한 결합이 발생한다는 것이다.
     * 우리는 의존하는 객체를 필요에 따라 교체할 수 있게 만들고 싶다.
     * 다른 구현체로 교체할 수 있도록 만들어보자.
     */
    @Test
    void stage1() {
        final var user = new User(1L, "gugu");

        final var userDao = new UserDao();

        /**
         * 클래스 내부에서 직접 객체를 생성하지 말고 외부에서 객체를 전달 받도록 수정했다.
         * 생성자를 통해서 객체를 전달 받도록 수정했다.
         * 하지만 아직도 테스트하기 어려운 코드다.
         * UserDao라는 구현 클래스에 의존하고 있다.
         * 유연한 변경이 필요한 부분은 인터페이스를 사용하면 결합을 감소시킬 수 있다.
         */
        final var userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
