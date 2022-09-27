package concurrency.stage1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServlet {

    private final List<User> users = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(UserServlet.class);

    public UserServlet() {
        IntStream.range(1, 10000).forEach(i -> users.add(new User("name" + i)));
    }

    public void service(final User user) {
        join(user);
    }

    private void join(final User user) {
        logger.info("in join");
        if (!users.contains(user)) {
            users.add(user);
        }
        logger.info("end join");
    }

    public int size() {
        return users.size();
    }

    public List<User> getUsers() {
        return users;
    }
}
