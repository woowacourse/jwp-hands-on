# Thread-safe class

쓰레드들 간에 자원 공유에 따른 문제들이 발생하지 않도록 클래스를 설계하는 방법!

1. 상태 변수를 쓰레드 간에 공유하지 않는다!

- 애초에 클래스에 상태 변수 자체를 두지 않는다.
- 존재하는 경우 접근할 수 없도록 한다.

2. 상태 변수를 변경할 수 없도록 만든다!

- `final`로 재할당 불가능하도록!
- `불변 객체`로 해당 필드들도 전부 불변으로!

3. 상태 변수에 접근할 때에 언제나 동기화(synchronization)를 사용한다.

- 스프링 자체에서 내부적으로 동기화 처리를 해줌!
- 실제로 `synchronized` 키워드를 사용할 일은 없음!

4. 캡슐화나 데이터 은닉은 Thread-safe class 작성에 도움이 된다.

- 객체지향적으로 코드를 짜는 것!

---

## 상태 없는 서블릿

상태가 없는 객체는 항상 Thread-safe하다!

- 인스턴스 변수가 없는 객체!
- 오직 메서드 내부에서 모든 로직을 처리! 메서드 내부의 **지역변수는 프로세스의 공유 자원이 아니라 각 쓰레드에서 만드는 콜스택에서 처리**됨!

```java
public class StatelessFactorizer implements Servlet {
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }
}
```

---

## 스프링 빈

- 스프링에서 빈으로 다루는 객체들은 기본적으로 `SingletonRegistry`에 저장됨!
- 여러 쓰레드에서 접근할 수 있으므로 상태 없는 객체로 만들거나 불변객체로 만들어야 함!
