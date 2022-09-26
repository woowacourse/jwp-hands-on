# DI 컨테이너 구현하기

## 학습목표

- 스프링 프레임워크의 핵심 기술인 DI 컨테이너를 구현한다.
- DI 컨테이너를 직접 구현하면서 스프링 DI에 대한 이해도를 높인다.

## DI 컨테이너란?

- Simple Spring DI
- 스프링 DI는 뭐지?
    - 스프링 의존성 주입
    - 오늘은 학습 테스트 코드를 통해 직접 만들어보자.

## 스프링 IoC의 용어 정리

- 스프링 용어가 비슷한 듯 조금씩 다르다. 용어를 한번 정리해보자.
    - 토비의 스프링 발췌
- 제어의 역전(IoC, Inversion of Control)
    - 모든 제어 권한을 자신이 아닌 다른 대상에게 위임한다.
- 빈(bean)
    - 스프링이 IoC 방식으로 관리하는 객체.
    - 스프링이 직접 생성과 제어를 담당하는 객체.
- 빈 팩터리(bean factory)
    - 스프링의 IoC를 담당하는 핵심 컨테이너
    - 빈을 등록하고, 생성하고, 조회하고, 반환하는 등 빈을 관리하는 기능을 담당한다.
- 애플리케이션 컨텍스트(application context)
    - 스프링에서 빈 팩터리를 확장한 IoC 컨테이너다.
    - 빈 팩터리는 주로 빈의 생성과 제어의 관점에서 이야기하는 것
    - 애플리케이션 컨텍스트는 스프링이 제공하는 애플리케이션 지원 기능을 모두 포함
- 컨테이너(container) 또는 IoC 컨테이너
    - IoC 방식으로 빈을 관리한다는 의미에서 애플리케이션 컨텍스트나 빈 팩터리를 말한다.

## 학습 순서

- DI 컨테이너로 변화하는 과정을 코드로 학습하자.
- di 모듈의 테스트 코드에 di 패키지가 있다.
- stage0부터 stage2까지 변화 과정은 강의에서 설명
- stage3, stage4의 테스트 코드가 정상 동작하도록 DIContainer를 구현한다.

## 0단계 - 정적 참조

- 서비스, DAO 클래스를 static 키워드를 사용하여 구현
- 문제점
    - [ ] 객체스럽지 않은 코드
    - [ ] DB 없이 테스트하기 어렵다.
    - [ ] DAO 객체를 교체하려면
        - [ ] 다른 DAO 객체를 직접 생성하고
        - [ ] 다른 DAO로 변경해서 다시 컴파일해야 한다.

```java
class Stage1Test {

    @Test
    void stage0() {
        final var user = new User(1L, "gugu");

        final var actual = UserService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
```

## 1단계 - 생성자 주입

- 서비스 클래스의 생성자 파라미터에 DAO 객체를 전달한다.
- 문제점
- [x] 객체스럽지 않은 코드
- [ ] DB 없이 테스트하기 어렵다.
- [ ] DAO 객체를 교체하려면
    - [ ] 다른 DAO 객체를 직접 생성하고
    - [ ] 다른 DAO로 변경해서 다시 컴파일해야 한다.
- [ ] 객체 관계를 일일이 설정해줘야 한다.

```java
class Stage1Test {

    @Test
    void stage1() {
        final var user = new User(1L, "gugu");

        final var userDao = new UserDao();
        final var userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
```

## 2단계 - 인터페이스로 생성자 주입

- DAO를 인터페이스로 확장한다.
- 서비스 클래스의 생성자 파라미터에 DAO의 구현체를 전달한다.
- 문제점
- [x] 객체스럽지 않은 코드
- [x] DB 없이 테스트하기 어렵다.
- [ ] DAO 객체를 교체하려면
    - [ ] 다른 DAO 객체를 직접 생성하고
    - [ ] 다른 DAO로 변경해서 다시 컴파일해야 한다.
- [ ] 객체 관계를 일일이 설정해줘야 한다.

```java
class Stage2Test {

    @Test
    void stage2() {
        final var user = new User(1L, "gugu");

        final UserDao userDao = new InMemoryUserDao();
        final var userService = new UserService(userDao);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
```

## DI Container

- DI Container?
    - 객체와 객체 간의 의존 관계를 설정한다.
        - 언제? 컴파일 말고 런타임
- 서비스, DAO 객체는 하나만 존재해도 충분하다.
    - 서버 환경에서 최적의 성능을 내기 위해서 싱글톤 패턴 사용

- 해결!
- [x] 객체스럽지 않은 코드
- [x] DB 없이 테스트하기 어렵다.
- [x] DAO 객체를 교체하려면
    - [x] 다른 DAO 객체를 직접 생성하고
    - [x] 다른 DAO로 변경해서 다시 컴파일해야 한다.
- [x] 객체 관계를 일일이 설정해줘야 한다.

## 실습 요구 사항

- stage3, stage4의 테스트 코드가 정상 동작하도록 DIContainer를 구현한다.

## 3단계

- 생성자 파라미터로 Set<Class<?>>를 전달하자.
- 전달 받은 클래스를 객체로 생성한다.
- 객체의 내부 필드의 타입에 맞는 객체(baen)를 찾아서 대입(assign)한다.
- DI에서 관리하는 객체(bean)를 찾아서 반환한다.

```java
class Stage3Test {

    @Test
    void stage3() {
        final var user = new User(1L, "gugu");

        final var diContainer = createDIContainer();
        final var userService = diContainer.getBean(UserService.class);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }

    private static DIContainer createDIContainer() {
        final var classes = new HashSet<Class<?>>();
        classes.add(InMemoryUserDao.class);
        classes.add(UserService.class);
        return new DIContainer(classes);
    }
}
```

## 4단계

- stage3에서 구현한 기능을 기본적으로 제공한다.
- 생성자 파라미터로 패키지명을 받아서 클래스를 찾는 ClassPathScanner를 구현한다.
- @Service, @Repository가 존재하는 클래스만 객체로 생성한다.
- 객체에서 @Inject를 붙인 필드만 필터하고 타입에 맞는 객체(bean)를 찾아서 대입(assign)한다.
- DI에서 관리하는 객체(bean)를 찾아서 반환한다.

```java
class Stage4Test {

    @Test
    void stage4() {
        final var user = new User(1L, "gugu");

        final var diContainer = createDIContainer();
        final var userService = diContainer.getBean(UserService.class);

        final var actual = userService.join(user);

        assertThat(actual.getAccount()).isEqualTo("gugu");

    }

    private static DIContainer createDIContainer() {
        final var rootPackageName = Stage4Test.class.getPackage().getName();
        return DIContainer.createContainerForPackage(rootPackageName);
    }
}
```

## 힌트

```java
// stage3 
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return null;
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    // 각 필드에 빈을 대입(assign)한다.
    private void setFields(final Object bean) {

    }

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return null;
    }
} 
```

```java
// stage4 
// stage3 코드에 아래 메서드를 추가한다. 
class DIContainer {
    // ...
    // ClassPathScanner 클래스를 활용해보자. 
    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return null;
    }
    // ... 
} 
```

```java
public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        return null;
    }
}
```
