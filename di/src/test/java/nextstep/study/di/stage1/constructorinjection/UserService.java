package nextstep.study.di.stage1.constructorinjection;

import nextstep.study.User;

class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User join(User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
