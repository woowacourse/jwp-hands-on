# Thread 활용하기 (실습)

- [학습 자료 및 정리](./doc/README.md)

## 학습목표

- 자바 스레드를 사용해보고 어떤 특징을 가지고 있는지 실습한다.
- 톰캣의 스레드를 이해하고 설정할 수 있다.

## 학습 순서

- 학습 테스트에서 스레드를 직접 생성해본다.
- 스레드 간에 상태가 어떻게 공유되는지 확인한다.
- 임베디드 톰캣의 스레드의 적절한 설정값을 적용한다.

## 강의

[Concurrency](https://www.youtube.com/watch?v=Ev9Y5LRQaAM&feature=emb_title)

## 실습 요구 사항

- 스레드를 사용하면서 생길 수 있는 동시성 이슈를 경험해보고 어떻게 해결할 수 있을지 고민해보자.
- 스프링부트에서 톰캣의 스레드를 설정한다.

## 0단계 - 스레드 이해하기

다음 순서로 학습 클래스의 테스트를 통과시켜보자.

1. ThreadTest
2. SynchronizationTest
3. ThreadPoolsTest

## 1단계 - 동시성 이슈 확인하기

- 스레드를 다룰 때 어떤 상황이 발생할 수 있을지 학습한다.
- ConcurrencyTest 클래스의 설명을 읽어보자.
- 상황 설명
  - 웹사이트에서 회원 가입을 하고 있다.
    - 동일한 이름으로 가입할 수 없도록 UserServlet에 if절로 중복을 체크하고 있다.
    - 스레드를 2개 만들어 동시에 접근해도 테스트는 정상적으로 통과된다.
    - 하지만 특정 상황에서 UserServlet의 중복 체크가 동작하지 않아 동일한 이름으로 가입하는 상황이 발생할 수 있다.
- 아래 영상을 참고하여 스레드의 동시성 이슈를 발생시켜보자.
- 왜 이런 문제가 발생했고 해결 방법이 무엇일지 다른 크루들과 논의해본다.

- [자바 쓰레드 동시성 문제](https://www.youtube.com/watch?v=3C9G4SeyfWM&feature=emb_title)

## 2단계 - WAS에 스레드 설정하기

- thread/src/main/resources/application.yml 파일을 확인한다.
- accept-count, max-connections, threads.max 가 각각 무엇을 의미하는지 찾아본다.
  - 톰캣의 공식 문서를 찾아 설명을 읽어본다.
  - 설명을 봐도 모르겠다. 실제 코드로 확인해본다.
    - accept-count, threads.max는 4단계 미션에서 언급했던 설정이다.
    - max-connections은 NIO 개념이 필요하여 제외했다.
  - 본인이 설명을 제대로 이해했는지 확인해본다.
    - application.yml에서 accept-count, max-connections, threads.max의 값을 하나씩 바꿔가면서 어떤 차이가 있는지 직접 눈으로 확인하라.

## 톰캣 스레드 설정 테스트 방법

1. concurrency.stage2.App 클래스에서 main 메서드를 실행하여 서버를 띄운다.
2. concurrency.stage2.AppTest 클래스의 주석을 참고하여 test() 메서드로 서버를 호출한다.
