package nextstep.study.di.stage3.context;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudyTest {

    @Test
    @DisplayName("추상화된 인스턴스를 찾는다.")
    void find() {
        // given
        final Class<UserDao> interfaceType = UserDao.class;
        final InMemoryUserDao concreteClass = new InMemoryUserDao();

        // when
        final boolean actual = interfaceType.isInstance(concreteClass);

        // then
        assertThat(actual).isTrue();
    }
}
