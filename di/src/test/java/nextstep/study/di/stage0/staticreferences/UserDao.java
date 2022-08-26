package nextstep.study.di.stage0.staticreferences;

import nextstep.study.User;

import java.util.HashMap;
import java.util.Map;

class UserDao {

    private static final Map<Long, User> users = new HashMap<>();

    public static void insert(User user) {
        users.put(user.getId(), user);
    }

    public static User findById(long id) {
        return users.get(id);
    }
}
