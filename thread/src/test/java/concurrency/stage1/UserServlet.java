package concurrency.stage1;

import java.util.ArrayList;
import java.util.List;

public class UserServlet {

    private final List<User> users = new ArrayList<>();

    public void service(final User user) {
        join(user);
    }

    private void join(final User user) {
        if (!users.contains(user)) {
            // 쓰레드A에서 users.add(user)가 호출되기 전인 경우, 쓰레드B도 여기에 도달!
            // 평싱시에는 쓰레드A에서 join이 전부 실행된 이후에 쓰레드B가 join을 호출해서 문제 없던 것!
            users.add(user); // Suspend: Thread 걸고 디버그로 테스트 실행시 fail!
        }
    }

    public int size() {
        return users.size();
    }

    public List<User> getUsers() {
        return users;
    }
}
