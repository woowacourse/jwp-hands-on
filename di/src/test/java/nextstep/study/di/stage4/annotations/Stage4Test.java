package nextstep.study.di.stage4.annotations;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage4Test {

    /**
     * stage3에서 만든 DIContainer도 충분히 좋지만 빈(bean)으로 등록할 객체를 직접 골라서 코드에 작성해야 하는 문제가 있다.
     * 같은 패키지에 있는 @Inject, @Service, @Repository를 찾아서 객체를 만들고 관계를 설정하도록 수정해보자.
     * 해당 역할은 ClassPathScanner 클래스에게 맡기자.
     */
    @Test
    void stage4() {
        final var user = new User(1L, "gugu");

        final var diContext = createDIContainer();
        final var userService = diContext.getBean(UserService.class);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    private static DIContainer createDIContainer() {
        final var rootPackageName = Stage4Test.class.getPackage().getName();
        return DIContainer.createContainerForPackage(rootPackageName);
    }
}
