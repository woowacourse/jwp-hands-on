package nextstep.study.di.stage4.annotations;

import nextstep.study.User;

interface UserDao {

    void insert(User user);

    User findById(long id);
}
