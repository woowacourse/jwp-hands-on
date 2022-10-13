package aop.service;

import aop.DataAccessException;
import aop.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TxUserService implements UserService {

    private final PlatformTransactionManager transactionManager;
    private final UserService userService;

    public TxUserService(final PlatformTransactionManager transactionManager, final UserService userService) {
        this.transactionManager = transactionManager;
        this.userService = userService;
    }

    @Override
    public User findById(final long id) {
        return userService.findById(id);
    }

    @Override
    public void insert(final User user) {
        userService.insert(user);
    }

    @Override
    public void changePassword(final long id, final String newPassword, final String createBy) {
        /* ===== 트랜잭션 영역 ===== */
        final var transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
        /* ===== 트랜잭션 영역 ===== */

        /* ===== 애플리케이션 영역 ===== */
            userService.changePassword(id, newPassword, createBy);
        /* ===== 애플리케이션 영역 ===== */

        /* ===== 트랜잭션 영역 ===== */
        } catch (RuntimeException e) {
            transactionManager.rollback(transactionStatus);
            throw new DataAccessException(e);
        }
        transactionManager.commit(transactionStatus);
        /* ===== 트랜잭션 영역 ===== */
    }
}
