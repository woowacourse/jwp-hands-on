package nextstep.study.di.stage1.constructorinjection;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage1Test {

    @Test
    void stage1() {
        final var user = new User(1L, "gugu");

        final var userDao = new UserDao();
        final var userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
