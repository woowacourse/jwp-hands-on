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
    - 클라이언트 코드에서 직접하던 작업을 외부의 IoC 역할을 맡은 주체에게 넘기는 것
    - WAS도 제어의 역전 개념이 적용됨. 개발자들은 서블릿을 구현하여 war 파일 만들어서 이미 개발된 웹서버에서 구동시킴.
    - 구구왈 거의 DI와 같은 개념 - (다르지 않나?)
- 빈(bean)
    - 스프링이 IoC 방식으로 관리하는 객체.
    - 스프링이 직접 생성과 제어를 담당하는 객체.
- 빈 팩터리(bean factory)
    - 스프링의 IoC를 담당하는 핵심 컨테이너
    - 빈을 등록하고, 생성하고, 조회하고, 반환하는 등 빈을 관리하는 기능을 담당한다.
    - 인테페이스. 이를 구현한 것이 애플리케이션 컨텍스트.
- 애플리케이션 컨텍스트(application context)
    - 스프링에서 빈 팩터리를 확장한 IoC 컨테이너다.
    - 빈 팩터리는 주로 빈의 생성과 제어의 관점에서 이야기하는 것
    - 애플리케이션 컨텍스트는 스프링이 제공하는 애플리케이션 지원 기능을 모두 포함
  - 공식문서: 1.16 Bean Factory or ApplicationContext 참고
- 컨테이너(container) 또는 IoC 컨테이너
    - IoC 방식으로 빈을 관리한다는 의미에서 애플리케이션 컨텍스트나 빈 팩터리를 말한다.

- cf) xxxx container : 객체를 관리하는 곳. 객체의 라이프사이클을 관리.

## 스프링 DI가 제공하는 가치는 무엇일까

- 토비의 스프링 1권에서 발췌
  - p.378~379

> 그래서 적절하게 책임과 관심이 다른 코드를 분리하고, 서로 영향을 주지 않도록 다양한 추상화 기법을 도입하고, 
> 애플리케이션 로직과 기술/환경을 분리하는 등의 작업은 갈수록 복잡해지는 엔터프라이즈 애플리케이션에는 반드시 필요하다. 
> 이를 위한 핵심적인 도구가 바로 스프링이 제공하는 DI다. 
> 스프링의 DI가 없었다면 인터페이스를 도입해서 나름 추상화를 했더라도 적지 않은 코드 사이의 결합이 남아있게 된다.

> 스프링의 의존관계 주입 기술인 DI는 모든 스프링 기술의 기반이 되는 핵심 엔진이자 원리이며, 
> 스프링이 지지하고 지원하는, 좋은 설계와 코드를 만드는 모든 과정에서 사용되는 가장 중요한 도구다. 
> 스프링을 DI 프레임워크라고 부르는 이유는 외부 설정정보를 통한 런타임 오브젝트 DI라는 단순한 기능을 제공하기 때문이 아니다. 
> 오히려 스프링이 DI에 담긴 원칙과 이를 응용하는 프로그래밍 모델을 자바 엔터프라이즈 기술의 많은 문제를 해결하는 데 적극적으로 활용하고 있기 때문이다. 
> 또, 스프링과 마찬가지로 스프링을 사용하는 개발자가 만드는 애플리케이션 코드 또한 
> 이런 DI를 활용해서 깔끔하고 유연한 코드와 설계를 만들어낼 수 있도록 지원하고 지지해주기 때문이다.

## 학습 순서

- DI 컨테이너로 변화하는 과정을 코드로 학습하자.
- di 모듈의 테스트 코드에 di 패키지가 있다.
- stage3, stage4의 테스트 코드가 정상 동작하도록 DIContainer를 구현한다.

## 0단계 - 정적 참조

- 서비스, DAO 클래스를 static 키워드를 사용하여 구현
- 문제점
    - 객체스럽지 않은 코드
    - DB 없이 테스트하기 어렵다.
    - DAO 객체를 교체하려면
        - 다른 DAO 객체를 직접 생성하고
        - 다른 DAO로 변경해서 다시 컴파일해야 한다.

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
- ~~객체스럽지 않은 코드~~
- DB 없이 테스트하기 어렵다.
- DAO 객체를 교체하려면
    - 다른 DAO 객체를 직접 생성하고
    - 다른 DAO로 변경해서 다시 컴파일해야 한다.
- 객체 관계를 일일이 설정해줘야 한다.

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
- ~~객체스럽지 않은 코드~~
- ~~DB 없이 테스트하기 어렵다.~~
- DAO 객체를 교체하려면
    - 다른 DAO 객체를 직접 생성하고
    - 다른 DAO로 변경해서 다시 컴파일해야 한다.
- 객체 관계를 일일이 설정해줘야 한다.

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
    - 컴파일 말고 **런타임에 객체와 객체 간의 의존 관계를 설정**한다.
- 서비스, DAO 객체는 하나만 존재해도 충분하다.
    - 서버 환경에서 최적의 성능을 내기 위해서 **싱글톤 패턴** 사용

- 해결!
- ~~객체스럽지 않은 코드~~
- ~~DB 없이 테스트하기 어렵다.~~
- ~~DAO 객체를 교체하려면~~
    - ~~다른 DAO 객체를 직접 생성하고~~
    - ~~다른 DAO로 변경해서 다시 컴파일해야 한다.~~
- ~~객체 관계를 일일이 설정해줘야 한다.~~

## 3단계

- 생성자 파라미터로 Set<Class<?>>를 전달하자.
- 전달 받은 클래스를 객체로 생성한다.
- 객체의 내부 필드의 타입에 맞는 객체(bean)를 찾아서 대입(assign)한다.
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
