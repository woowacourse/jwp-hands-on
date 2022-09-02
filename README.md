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

### 2단계 - ETag/If-None-Match 적용하기

- ETag
    - HTTP 응답에서 특정 버전의 리소스를 식별하는 식별자
    - 웹서버가 내용을 확인하고 변하지 않았다면 캐시 유지, 변했다면 덮어쓰기 및 새로운 ETag 생성
    - 유효기간이 지난 예전의 ETag를 가지고 해당 캐시를 수정하려고 한다면 **412 Precondition Failed** 반환
    - 유일한 값으로써 다른 서버들이 추적하는 용도로도 이용 가능

- If-None-Match
    - ETag가 같다면 서버는 **304 Not Modified**
      를 응답하여 캐시를 그대로 사용하고, 다르다면 컨텐츠를 새로 내려주는 설정
    - etag 값을 담고있음

- ShallowEtagHeaderFilter

  : 스프링이 제공하는 etag 관련 필터

    - request, response에 etag 부여 및 검증
    - etag가 존재하는 동일 URI 요청시 자동으로 If-None-Match를 헤더에 추가

### 3단계 - 캐시 무효화 ( Cache Busting )

- 자주 변경되는 파일의 경우 캐시에 의해 값이 깨질 수 있다. 이때, 각 파일명을 버전에 따라 다르게 이용한다면 uri가 변경되므로
  손쉽게 캐시를 제거할 수 있다!
    - ex) 자바스크립트와 css에서 두 파일의 버전이 동기화되지 않는 경우
- 이렇듯 파일이 변경될 때 url도 함께 변경됨으로써 기존의 캐시를 삭제하고 새로운 파일을 저장하는 방법을 캐시 무효화라고 한다.
- 구현 방법
  1. 정적 리소스 파일의 max-age를 최대치(1년)로 설정
  2. ETag 적용
  3. 리소스 파일에 변경사항이 생기면 버전 적용
  4. 캐시 추가
