# AOP 경험하기

## 학습 목표

- 메서드 단위의 트랜잭션 AOP를 구현해본다.
- 스프링에서 AOP를 어떻게 지원하는지 학습한다.

## 학습 순서

- AOP 개념과 용어를 학습한다.
- 트랜잭션 AOP를 단계별로 구현한다.

## AOP란?

- AOP(Aspect-Oriented Programming)는 프로그램 구조에 대한 또 다른 사고 방식을 제공하여 객체 지향 프로그래밍(OOP)을 보완합니다.
- OOP에서 모듈화의 핵심 단위는 클래스인 반면, AOP에서 모듈화의 단위는 측면입니다.
- Aspect는 여러 유형과 객체를 가로지르는 문제(트랜잭션 관리 등)의 모듈화를 가능하게 합니다.

## AOP 용어

- 애스펙트(Aspect)
    - AOP의 기본 모듈
    - 한 개 이상의 포인트컷과 어드바이스를 갖고 있다.
- 조인 포인트(Join point)
    - 어드바이스를 적용할 위치
- 어드바이스(Advice)
    - 부가기능을 담고 있는 모듈
    - 스프링은 어드바이스를 인터셉터로 모델링하고 조인 포인트 주변에 인터셉터 체인을 유지한다.
- 포인트 컷(Pointcut)
    - 어드바이스를 적용할 조인 포인트를 선별하는 모듈
- 타겟(Target)
    - 부가기능(advice)을 적용할 대상
- 프록시(Proxy)
    - 클라이언트와 타겟 사이에서 부가기능을 제공하는 객체
    - 스프링에서 프록시는 JDK 동적 프록시 또는 CGLIB 프록시로 구현된다.

[AOP Concepts 원문](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-introduction-defn)

## 실습 요구 사항

- @Transactional을 구현한다.
    - 메서드 단위의 트랜잭션만 처리하도록 만들자.
- 모든 테스트를 통과시킨다.

## 0단계 - JDK Proxy로 프록시 패턴 적용하기

- 3단계 미션에서 서비스 추상화를 활용하여 부가 기능인 트랜잭션과 핵심 기능인 비즈니스 로직을 분리했다.
- 트랜잭션은 UserService 외에 다른 서비스 객체에서도 자주 사용된다.
- 지금 구조는 서비스 객체마다 인터페이스를 만들고 트랜잭션 서비스를 구현해야 한다.
- JDK Proxy를 활용하여 매번 트랜잭션 클래스를 동적(dynamic)으로 만들면 매번 트랜잭션 서비스를 구현할 필요가 없다.
- 핵심 기능과 부가 기능을 아래 클래스로 나눠서 구현해보자.
    - 핵심 기능 AppUserService
    - 부가 기능 TransactionHandler
- ❗️스프링이 아닌 java.lang.reflect.Proxy 클래스를 사용해서 프록시를 구현한다.

## 1단계 - ProxyFactoryBean 적용하기

- stage0에서 Proxy를 사용하여 비즈니스 로직에 트랜잭션을 추가할 수 있었다.
- 하지만 몇 가지 아쉬운 점이 남는다.
    - 트랜잭션을 사용하려면 서비스 객체에 인터페이스가 필요하다.(JDK Proxy는 인터페이스가 필수)
    - 프록시를 생성할 때 인터페이스를 직접 지정해야 한다.
    - 서비스마다 프록시를 직접 생성하기 때문에 생성자를 활용한 스프링 빈 생성이 어렵다.
- 스프링에서 제공하는 ProxyFactoryBean 클래스를 사용하여 문제를 해결해보자.
- 참고로 ProxyFactoryBean 객체는 2가지 일을 한다.
    - FactoryBean 객체는 생성자가 아닌 다른 방식(정적 팩터리 메서드)으로 스프링 빈을 생성할 때 사용한다.
        - stage0 코드를 잘 살펴보면 JDK Proxy는 생성자가 아닌 정적 팩터리 메서드로 객체를 생성하고 있다.
    - 프록시 객체를 생성한다.
        - 스프링 프레임워크는 기본적으로 JDK Proxy를 사용한다.
            - JDK Proxy의 문제는 인터페이스가 필수고 메서드만 사용하여 프록시를 생성한다.
    - proxyTargetClass를 true로 바꾸면 CGLib를 사용하여 프록시를 생성할 수 있다.
        - 스프링 부트는 CGLib가 기본이다.
        - CGLib는 인터페이스가 아닌 클래스로 프록시를 생성할 수 있다.
- AOP 용어가 몇 가지 보인다.
    - pointcut, advice, advisor가 무엇인지 학습한다.
    - TransactionPointcut, TransactionAdvice, TransactionAdvisor 클래스를 각각 구현한다.
    - 그리고 ProxyFactoryBean 객체에 Advisor로 등록한다.

## 2단계 - 스프링 AOP에 추가하기

- stage1을 마치고 나니 AOP 관련 용어들이 눈에 띈다.
- 실습을 통해 스프링은 프록시를 이용해서 AOP를 지원한다는 것을 알 수 있었다.
- stage1에서 스프링이 제공하는 ProxyFactoryBean을 사용하여 트랜잭션 AOP를 구현했다.
- 하지만 여전히 아쉬운 점이 남아있다.
    - 지금 구조는 트랜잭션을 적용할 타겟 객체를 매번 ProxyFactoryBean 클래스를 사용해서 직접 지정해줘야 한다.
- 프록시 생성에 사용한 클래스를 DI에 스프링 빈으로 등록하여 자동으로 프록시를 생성하도록 만들어보자.
    - AopConfig 클래스에 stage1에서 구현한 클래스를 스프링 빈으로 등록한다.
    - DefaultAdvisorAutoProxyCreator 클래스도 스프링 빈으로 추가하자.
