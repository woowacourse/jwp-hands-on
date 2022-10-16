package aop.stage1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aop.DataAccessException;
import aop.StubUserHistoryDao;
import aop.domain.User;
import aop.repository.UserDao;
import aop.repository.UserHistoryDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Stage1Test {

    private static final Logger log = LoggerFactory.getLogger(Stage1Test.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserHistoryDao userHistoryDao;

    @Autowired
    private StubUserHistoryDao stubUserHistoryDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @BeforeEach
    void setUp() {
        final var user = new User("gugu", "password", "hkkang@woowahan.com");
        userDao.insert(user);
    }

    /**
     * ProxyFactoryBean: FactoryBean implementation that builds an AOP proxy based on beans in a Spring BeanFactory.
     *
     * [참고 블로그](https://gmoon92.github.io/spring/aop/2019/02/23/spring-aop-proxy-bean.html)
     */
    @Test
    void testChangePassword() {
        final UserService userService = createTransactionProxy(new UserService(userDao, userHistoryDao));

        final var newPassword = "qqqqq";
        final var createBy = "gugu";
        userService.changePassword(1L, newPassword, createBy);

        final var actual = userService.findById(1L);

        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void testTransactionRollback() {
        final UserService userService = createTransactionProxy(new UserService(userDao, stubUserHistoryDao));

        final var newPassword = "newPassword";
        final var createBy = "gugu";
        assertThrows(DataAccessException.class,
            () -> userService.changePassword(1L, newPassword, createBy));

        final var actual = userService.findById(1L);

        assertThat(actual.getPassword()).isNotEqualTo(newPassword);
    }


    @SuppressWarnings("unchecked")
    private <T> T createTransactionProxy(final T target) {
        final var proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);

        // jdk proxy 대신에 cglib를 사용: 인터페이스가 아닌 클래스를 대상으로 프록시를 만들 때 필요한 설정.
        proxyFactoryBean.setProxyTargetClass(true);

        final var pointcut = new TransactionPointcut();
        final var advice = new TransactionAdvice(platformTransactionManager);
        proxyFactoryBean.addAdvisor(new TransactionAdvisor(pointcut, advice));

        return (T) proxyFactoryBean.getObject();
    }
}
