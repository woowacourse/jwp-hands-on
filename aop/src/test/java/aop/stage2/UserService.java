package aop.stage2;

import aop.Transactional;
import aop.domain.User;
import aop.domain.UserHistory;
import aop.repository.UserDao;
import aop.repository.UserHistoryDao;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;
    private UserHistoryDao userHistoryDao;

    public UserService(final UserDao userDao, final UserHistoryDao userHistoryDao) {
        this.userDao = userDao;
        this.userHistoryDao = userHistoryDao;
    }

    @Transactional
    public User findById(final long id) {
        return userDao.findById(id);
    }

    @Transactional
    public void insert(final User user) {
        userDao.insert(user);
    }

    @Transactional
    public void changePassword(final long id, final String newPassword, final String createBy) {
        final var user = findById(id);
        user.changePassword(newPassword);
        userDao.update(user);
        userHistoryDao.log(new UserHistory(user, createBy));
    }

    public void setUserHistoryDao(final UserHistoryDao userHistoryDao) {
        this.userHistoryDao = userHistoryDao;
    }
}
