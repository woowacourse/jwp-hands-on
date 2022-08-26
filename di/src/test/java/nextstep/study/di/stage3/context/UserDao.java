package nextstep.study.di.stage3.context;

import nextstep.study.User;

interface UserDao {

    void insert(User user);

    User findById(long id);
}
