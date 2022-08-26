package nextstep.study.di.stage4.annotations;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage4Test {

    @Test
    void stage4() {
        final var user = new User(1L, "gugu");

        final var diContext = createDIContext();
        final var userService = diContext.getBean(UserService.class);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    private static DIContext createDIContext() {
        final var rootPackageName = Stage4Test.class.getPackage().getName();
        return DIContext.createContextForPackage(rootPackageName);
    }
}
