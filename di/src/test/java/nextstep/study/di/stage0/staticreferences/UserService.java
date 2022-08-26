package nextstep.study.di.stage0.staticreferences;

import nextstep.study.User;

class UserService {

    public static User join(User user) {
        UserDao.insert(user);
        return UserDao.findById(user.getId());
    }
}
