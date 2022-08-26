package nextstep.study.di.stage4.annotations;

import nextstep.study.User;

@Service
class UserService {

    @Inject
    private UserDao userDao;

    public User join(final User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }

    private UserService() {}
}
