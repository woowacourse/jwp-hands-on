package nextstep.study.di.stage3.context;

import nextstep.study.User;

class UserService {

    private UserDao userDao;

    private UserService() {}

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public User join(final User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
