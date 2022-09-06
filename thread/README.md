### 동시성(Concurrency)

- 자바는 `동시성`을 지원한다(자바를 사용하는 서버에서 여러 사용자를 동시에 처리할 수 있어야 한다).
- 웹 서버는 `스레드`를 기반으로 동작한다.

### 프로세스 & 스레드

- 프로세스 : 프로그램, 응용 프로그램과 동의어로 간주
    - disk의 프로그램을 실행하면 ram에 프로세스로 올라간다.
    - 프로세스 내에 스레드가 실행된다.
    - 모든 프로세스는 하나 이상의 스레드를 가진다.
    - jvm은 단일 프로세스가 실행된다.
- 스레드 : 경량 프로세스라고 불림
    - 프로세스와 같이 실행환경을 제공한다.
    - 프로세스를 새로 만드는 것보다 스레드는 새로 만드는 것이 적은 리소스
    - 프로세스의 리소스를 공유한다.


## 동기화(Synchronization)

두 가지가 발생할 수 있다.

- 스레드 간섭(Thread Interference)
- 메모리 일관성 오류

하나씩 살펴보자

### 1. 스레드 간섭(Thread Interference)

```java
class Counter {
	private int c = 0;

	public void increment() {
		c++;
	}

	public void decrement() {
		c--;
	}
	
	public int value() {
		return c;
	}
}
```

1. 스레드 A가 increment() 실행 `c = 0 상태로 앎 → c = 1`
2. 스레드 B가 decrement() 동시 실행 `c = 0 상태에서 실행 -> c = -1 (덮어씀)`

   → 스레드 A의 결과가 `-1`


### 2. 메모리 일관성 오류

```java
public class UnsafeSequence {
	private int value;

	public void getNext() {
		return value++;
	}
}
```

- 스레드 A, B가 value 값을 사용할 때 동기화가 되지 않아 일치하지 않는 경우가 발생할 수 있음
    - 발생이전관계(happens-before-relationship)를 적용해야 해결할 수 있음


### 해결방법(Thread-safe class)

- 상태 변수를 스레드 간 공유하지 않는다.
- 상태 변수를 변경할 수 없도록 만든다.
- 상태 변수에 접근할 땐 언제나 동기화(synchronization)를 사용한다.
    - 응용프로그래머가 직접할 일은 거의없다(스프링이 해줌)
- 캡슐화나 데이터 은닉은 스레드 안전한 클래스 작성에 도움이 된다.

### 상태 없는 서블릿

- 상태 없는 객체는 항상 스레드 안전하다.

```java
// 스레드 안전한 클래스
// 인스턴스 변수를 사용하지 않는다. (지역변수만 사용)
public class StatelessFactorizer implements Servlet {
	public void service(ServletRequest req, ServletResponse resp) {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
		encodeIntoResponse(resp, factors);
	}
}
```

### 스레드 풀

작업 처리에 사용되는 `스레드를 제한된 개수만큼 정해` 놓고 작업 `큐(Queue)`에 들어오는 작업들을 `하나씩 스레드가 맡아 처리`한다.

### 다중 스레드 환경

- 두 개 이상의 스레드가 변경 가능한(mutable)공유 데이터를 동시에 업데이트하면 경쟁 조건(race condition)이 발생한다.
- 자바는 공유 데이터에 대한 스레드 접근을 동기화(`synchronization`)하여 경쟁 조건을 방지한다.
- 동기화된 블록은 하나의 스레드만 접근하여 실행할 수 있다.

```java
public synchronized void calculate() {
    setSum(getSum() + 1);
}
```

---

### HTTP의 특징

- Stateless
- 클라이언트 서버 모델

### HTTP는 왜 Stateless일까?

- 서버의 부하가 적다.

### 세션의 문제점

서버를 2대로 늘리면 세션은 어떻게 될까?

- A에서 사용하다가 B에서 접속하면 A로그인이 풀린다
    - Session Clustering: 웹 서버간 세션을 공유
    - Sticky Session: 로드밸런서 설정
    - JWT