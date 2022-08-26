package nextstep.study.di.stage2.constructorwithinterfaces;

import nextstep.study.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Stage2Test {

    @Test
    void stage2() {
        final var user = new User(1L, "gugu");

        final UserDao userDao = new InMemoryUserDao();
        final var userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    @Test
    void testAnonymousClass() {
        // given
        final var userDao = new UserDao() {
            private User user;

            @Override
            public void insert(User user) {
                this.user = user;
            }

            @Override
            public User findById(long id) {
                return user;
            }
        };
        final var userService = new UserService(userDao);
        final var user = new User(1L, "gugu");

        // when
        final var actual = userService.join(user);

        // then
        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
