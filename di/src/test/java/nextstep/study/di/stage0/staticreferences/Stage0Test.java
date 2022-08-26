package nextstep.study.di.stage0.staticreferences;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage0Test {

    @Test
    void stage0() {
        final var user = new User(1L, "gugu");

        final var actual = UserService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
