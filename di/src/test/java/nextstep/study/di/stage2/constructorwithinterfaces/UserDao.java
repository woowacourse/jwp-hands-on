package nextstep.study.di.stage2.constructorwithinterfaces;

import nextstep.study.User;

interface UserDao {

    void insert(User user);

    User findById(long id);
}
