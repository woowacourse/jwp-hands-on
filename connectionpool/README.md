# DB ConnectionPool 적용하기

## 학습 목표

- 애플리케이션이 필요로 할 때마다 매번 DB에 물리적 연결(Connection)을 만들면 시간과 리소스가 많이 사용된다.
- 서버 성능을 높이기 위해 Connection Pool을 사용하자.
- Connection Pool은 DB로부터 미리 Connection을 만들어 보관한다.

## 학습 순서

- JDBC 드라이버를 사용하여 데이터베이스에 연결하는 방법을 살펴본다.
- HikariCP가 어떤 것인지 다뤄보고 공식 문서를 읽어본다.
- MaximumPoolSize를 몇으로 설정하면 좋을지 고민하고 적용한다.

## 0단계 - DataSource 다루기

- 자바에서 제공하는 JDBC 드라이버를 직접 다뤄본다.
- 데이터베이스에 어떻게 연결하는지, 그리고 왜 DataSource를 사용하는지 찾아보자.

- [DataSource가 상위호환](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/javax/sql/package-summary.html)
  - Connection pooling
  - 분산 트랜잭션(Distributed Transactions)

## 1단계 - 커넥션 풀링

- 커넥션 풀링이 무엇인지 h2의 JdbcConnectionPool을 직접 다뤄본다.
    - JdbcConnectionPool 클래스가 복잡하지 않으니 직접 분석해봐도 좋다.
- 왜 스프링 부트에서 HikariCP를 사용하는지 찾아본다.
    - [spring boot docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#data.sql.datasource.connection-pool)
- 그리고 HikariCP에 어떤 설정을 하면 좋을지 공식 문서를 읽어보자.
    - [HikariCP](https://github.com/brettwooldridge/HikariCP#rocket-initialization)
    - [About Pool Sizing](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing)
    - [MySQL Configuration](https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration)

## 2단계 - HikariCP 설정하기

- 스프링 부트로 HikariCP를 설정하는 방법을 익힌다.
- 커넥션 풀이 의도한대로 동작하는지 테스트한다.
