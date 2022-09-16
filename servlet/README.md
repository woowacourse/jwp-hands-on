# 서블릿 구현하기

- [학습 자료 및 정리](./doc/README.md)

## 학습 목표

- 서블릿, 필터가 무엇인지 직접 경험해본다.
- 서블릿을 사용할 때 주의할 점을 학습한다.

## 학습 순서

- 서블릿이 무엇인지 학습한다.
- 학습한 내용을 바탕으로 실제 서블릿과 필터를 구현한 코드를 분석하고 테스트를 통과시킨다.

---

## 실습 요구 사항

ServletTest와 FilterTest 클래스의 모든 테스트를 통과시킨다.

## 1단계 - 서블릿 학습 테스트

- SharedCounterServlet, LocalCounterServlet 클래스를 열어보고 어떤 차이점이 있는지 확인한다.
- ServletTest를 통과시킨다.
- init, service, destroy 메서드가 각각 언제 실행되는지 콘솔 로그에서 확인한다.
- 왜 이런 결과가 나왔는지 다른 크루와 이야기해보자.
- 직접 톰캣 서버를 띄워보고 싶다면 ServletApplication 클래스의 main 메서드를 실행한다.
  - 웹 브라우저에서 localhost:8080/shared-counter 경로에 접근 가능한지 확인한다.

> 예전 서블릿 책을 보면 서블릿 클래스를 구현하고 xml에 등록하는 내용이 나온다.
  Servlet 3.0부터는 @WebServlet을 사용하면 된다.
  필터도 마찬가지로 @WebFilter를 사용한다.

## 2단계 - 필터 학습 테스트

- FilterTest를 통과시킨다.
- doFilter 메서드는 어느 시점에 실행될까? 콘솔 로그에서 확인한다.
- 왜 인코딩을 따로 설정해줘야 할까?
  - [ServletResponse](https://docs.oracle.com/javaee/7/api/javax/servlet/ServletResponse.html)
  - 위 링크에서 character encoding에 대한 설명을 참고하자.
