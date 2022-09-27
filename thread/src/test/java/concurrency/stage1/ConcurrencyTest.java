package concurrency.stage1;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * 스레드를 다룰 때 어떤 상황을 조심해야 할까? - 상태를 가진 한 객체를 여러 스레드에서 동시에 접근할 경우 - static 변수를 가진 객체를 여러 스레드에서 동시에 접근할 경우
 * <p>
 * 위 경우는 동기화(synchronization)를 적용시키거나 객체가 상태를 갖지 않도록 한다. 객체를 불변 객체로 만드는 방법도 있다.
 * <p>
 * 웹서버는 여러 사용자가 동시에 접속을 시도하기 때문에 동시성 이슈가 생길 수 있다. 어떤 사례가 있는지 아래 테스트 코드를 통해 알아보자.
 */
class ConcurrencyTest {

    @Test
    void test() throws InterruptedException {
        final var userServlet = new UserServlet();

        // 웹서버로 동시에 2명의 유저가 gugu라는 이름으로 가입을 시도했다.
        // UserServlet의 users에 이미 가입된 회원이 있으면 중복 가입할 수 없도록 코드를 작성했다.
        final User gugu = new User("gugu");
        final var firstThread = new Thread(new HttpProcessor(gugu, userServlet));
        final var secondThread = new Thread(new HttpProcessor(new User("gugu"), userServlet));

        // 스레드는 실행 순서가 정해져 있지 않다.
        // firstThread보다 늦게 시작한 secondThread가 먼저 실행될 수도 있다.
        firstThread.start();
        secondThread.start();
        secondThread.join(); // secondThread가 먼저 gugu로 가입했다.
        firstThread.join();

        // 이미 gugu로 가입한 사용자가 있어서 UserServlet.join() 메서드의 if절 조건은 false가 되고 크기는 1이다.
        // 하지만 디버거로 개별 스레드를 일시 중지하면 if절 조건이 true가 되고 크기가 2가 된다. 왜 그럴까?

        // 왜 디버거로 일시정지 하면 크기가 2개가 되지?
        // 단순히 if(contains()) 하고 user를 더하는 작업은 매우 빠르게 동작하기 때문에 다른 쓰레드가 들어오기 전에 먼저 들어온 쓰레드가 처리를 해버린다.
        // 그렇기 때문에 정상적으로 수행되지만 내부의 로직에 다른 로직들이 들어오게 되거나 contains에 시간이 오래걸리게 되거나 내부 함수에 디버깅 마크를 찍게되면 시간이 지연되기 때문에
        // contains를 검사하기 전에 다른 쓰레드가 회원가입을 하지 못함. 그래서 두 쓰레드 모두 contains를 통과해서 gugu가 두 번 회원가입 되는 문제가 발생한다.

        // 이를 해결하기 위한 가장 단순한 방법은 synchronize 메서드로 만들어주는 것!
        assertThat(userServlet.getUsers()
                .stream()
                .filter(it -> it.equals(gugu))
                .collect(Collectors.toList())).hasSize(2);
    }
}
