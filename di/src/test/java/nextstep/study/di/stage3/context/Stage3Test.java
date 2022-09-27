package nextstep.study.di.stage3.context;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import nextstep.study.User;
import org.junit.jupiter.api.Test;

class Stage3Test {

    /**
     * 객체를 생성하고 연결해주는 역할을 DIContainer라는 클래스에게 맡기자.
     * DIContainer는 애플리케이션을 구성하는 객체의 구조와 관계를 정의한 설계도 역할을 한다.
     * 어떤 객체가 어떤 객체를 사용하는지 정의하는 역할을 한다.
     * 테스트가 통과하도록 DIContainer 클래스를 구현하자.
     */
    @Test
    void stage3() {
        final var user = new User(1L, "gugu");

        final var diContainer = createDIContainer();

        /**
         * 제어의 역전(IoC)
         * stage2에서 객체는 능동적으로 자신이 사용할 클래스를 결정하고, 직접 객체를 생성했다.
         * 하지만 제어의 역전이라는 개념이 적용되면 객체는 자신이 사용할 객체를 선택하고 생성하지 않는다.
         * 모든 제어 권한을 자신이 아닌 다른 대상에게 위임한다.
         * UserService는 DIContainer에게 모든 제어 권한을 위임한 상태다.
         * DIContainer가 객체를 생성하고 관계를 설정하도록 구현해보자.
         */
        final var userService = diContainer.getBean(UserService.class);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    /**
     * DIContainer가 관리하는 객체는 빈(bean) 객체라고 부른다.
     */
    private static DIContainer createDIContainer() {
        var classes = new HashSet<Class<?>>();
        classes.add(InMemoryUserDao.class);
        classes.add(UserService.class);
        return new DIContainer(classes);
    }
}
