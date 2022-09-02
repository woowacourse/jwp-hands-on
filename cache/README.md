# CACHE

### HTTP CACHE 종류

- Private Cache - Web browser에 있는 캐쉬 → 사용자의 개인화된 응답을 저장할 수 있음, Server 응답에 Authorization 헤더가 있으면 cache 저장이 안됨
- Shared Cache - Proxy, Managed가 있음, Proxy는 조작 불가. Managed에는 Reverse Proxy, CDN이 있음

### 캐시 유효 기간

- Date로 부터 Cache-Control: max-age만큼 캐싱함
- fresh : 최신 상태, stale : 만료 상태

유효기간이 지난 캐시를 바로 삭제하지는 않는다. 오래된 응답을 계속해서 써도 되는지 서버에 확인하는 작업을 한다.

### 재검증

- 유효성 검증 또는 재검증
- 조건부 요청
    - If-Modified-Since
        - 날짜를 기준으로 검증
        - 서버에서 Last-Modified에 마지막으로 수정된 시간, Cache-Control에 캐시 지속 시간을 담아 클라이언트에게 전송
        - 클라이언트 측에서 만료가 되면 If-Modified-Since에 Last-Modified 시간을 담아 서버에 전송
        - 만약 서버측 자원에 변경이 없었다면 304 Not Modified를 내려줌
        - 초 단위로 검증하기 때문에 그 이하에 대해선 검증이 불가
    - ETag/If-None-Match
        - ETag에 어떠한 해쉬값, Cache-Control에 캐시 지속 시간을 담아 클라이언트에게 전송을 넣어 Tag값을 전송
        - 클라이언트 측에서 만료가 되면 If-None-Match에 ETag값을 넣어 서버에 전달
        - 만약 ETag값이 변경되지 않았다면 서버측에서 304 Not Modified를 내려줌
- 강제 재검증
    - 예전에는 서버측에서 Cache-Control에서 max-age=0, must-revalidate를 사용해 강제 재검증을 진행했음
    - HTTP 1.1에선 클라이언트 측에서 Cache-Control: no-cache를 담아서 보내면 강제로 재검증을 함

## 캐시 패턴

### 기본 설정

- Cache-Control: no-cache 또는 Cache-Control: no-cache, private를 사용하자.
- Cache-Control 헤더를 쓰지 않으면 휴리스틱 캐싱이 발생

### 캐시 무효화

- 보통 캐시는 변하지 않는 css나 js파일을 캐싱한다.
- 이 때, 캐쉬는 url을 기준으로 저장하기 때문에 만약 css나 js 파일을 수정하게 되어도 브라우저 측에선 캐싱된 css와 js파일을 사용한다
- 이 때, 캐시 무효화 전략이 필요하다. ex) bundle.js, build.css같은 경우 파일에 버전을 부여하거나(bundle.v.123.js, bundle.js?v=123) 파일에 해쉬값을 부여한다(
  bundle.YsAIAAAA-OPQFEFSADFASDF.js)
- 배포 시에만 version을 변경해 계속해 캐싱되어 사용될 수 있게 한다.
- 304 Not Modified가 아닌 200 OK
