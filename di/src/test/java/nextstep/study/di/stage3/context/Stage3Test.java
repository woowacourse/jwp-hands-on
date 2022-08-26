package nextstep.study.di.stage3.context;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class Stage3Test {

    @Test
    void stage3() {
        final var user = new User(1L, "gugu");

        final var diContext = createDIContext();
        final var userService = diContext.getBean(UserService.class);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    private static DIContext createDIContext() {
        var classes = new HashSet<Class<?>>();
        classes.add(InMemoryUserDao.class);
        classes.add(UserService.class);
        return new DIContext(classes);
    }
}
