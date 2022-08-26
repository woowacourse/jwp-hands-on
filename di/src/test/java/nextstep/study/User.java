package nextstep.study;

public class User {

    private final long id;
    private final String account;

    public User(long id, String account) {
        this.id = id;
        this.account = account;
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }
}
