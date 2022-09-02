# JWP Hands-On

## 만들면서 배우는 스프링 실습 코드

### 학습 순서

- cache
- thread
- servlet
- reflection
- di

## 새롭게 알게 된 내용 정리

### 0단계 - 휴리스틱 제거하기

- Cache-Control
    - 헤더 필드 중 하나로써 요청과 응답의 캐시 정책 정의 가능
    - `max-age= n`
        - 초 단위로 캐시의 수명 결정
    - `no-cache`
        - 캐시의 유효성 확인을 위해 매번 서버에게 요청
        - max-age가 0인 것과 같은 효과
        - no-store와 달리 캐시를 저장하지 않는 것이 아니라 저장하되 유효한지 매번 확인하는 것
    - `no-store`
        - 어떤 요청도 캐시로 저장하지 않음
    - `must-revalidate`
        - 캐시의 유효성을 검증할 때 프록시 서버가 아니라 오리진 서버에게 검증을 요청하는 방식.
        - 프록시 서버 단에서 캐시를 반환하는 것을 방지하기 위해 이용
    - `public`
        - 모든 응답/요청에 대해 캐시 저장
    - `private`
        - 타인과 공유될 수 있는 프록시 서버 등의 공유 캐시에는 저장되지 않음
        - 최종 사용자의 클라이언트에만 캐시 저장!
        - cache-control의 기본 설정

- 휴리스틱 캐싱
    - 응답 헤더에 Cache-Control이 없어도 웹 브라우저가 스스로 암시적인 캐싱을 진행하는 것
    - 여러 알고리즘으로 max-age를 계산한다. 가장 보편적으로 사용되는 것은 LM알고리즘!

### 1단계 - HTTP Compression 설정하기

- HTTP Compression
  : HTTP 응답을 gzip 등으로 압축하여 성능을 향상시키는 것!

    - application properties에서 `server.compression.enabled=true` 로 활성화시킴으로써 사용 가능.
    - 기본 설정으로는 길이가 2048바이트(2KB) 이상이고, 콘텐츠 유형이 다음 중 하나여야만 가능하다.
        - `text/html`
        - `text/xml`
        - `text/plain`
        - `text/css`
        - `text/javascript`
        - `application/javascript`
        - `application/json`
        - `application/xml`

    - 최소길이를 조정하고싶다면 `server.compression.min-response-size` 를 통해 수정 가능
    - 콘텐츠 유형을 조정하고싶다면 `server.compression.mime-types` 를 통해 수정 가능
    - 정상적으로 압축되었다면 응답 헤더에 `Content-Encoding: gzip` 이 추가되었을 것이다.
