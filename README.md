# Web Cache

> JWP Hands-On

### LMS

> https://techcourse.woowahan.com/s/cCM7rQR9/ls/cxmbDHT7

## 요구사항

- [x] 0단계 - 휴리스틱 캐싱 제거하기
  - `Cache-Control: no-cache, private` 로 설정
  - `testNoCachePrivate()`
- [x] 1단계 - HTTP Compression 설정하기
  - gzip이 적용됐는지 테스트 코드에서 확인
    - `testCompression()`
  - [x] 웹 브라우저에서 HTTP 응답의 헤더를 직접 확인
- [x] 2단계 - ETag/If-None-Match 적용하기
  - 필터를 사용하여 /etag 경로만 ETag를 적용
  - `testETag()`
- [ ] 3단계 - 캐시 무효화(Cache Busting)
  - js, css, ETag 에 캐싱 적용
  - `testCacheBustingOfStaticResources()`

어떤 경우에 HTTP 캐시를 적용하는게 좋을까?
JS, CSS 같은 정적 리소스 파일은 캐싱을 적용하기 좋다.
하지만 자바스크립트와 css는 개발이 진행됨에 따라 자주 변경되고, 자바스크립트와 CSS 리소스 버전이 동기화되지 않으면 화면이 깨진다.
캐시는 URL을 기반으로 리소스를 구분하므로 리소스가 업데이트될 때 URL을 변경하면 손쉽게 JS, CSS 파일의 캐시를 제거할 수 있다.
캐시 무효화는 콘텐츠가 변경될 때 URL을 변경하여 응답을 장기간 캐시하게 만드는 기술이다.
어떻게 적용할까?
정적 리소스 파일의 max-age를 최대치(1년)로 설정한다.
ETag도 적용한다.
JS, CSS 리소스에 변경사항이 생기면 캐시가 제거되도록 url에 버전을 적용하자.
CacheBustingWebConfig 클래스의 addResourceHandlers 메서드의 registry에 리소스 디렉터리에 버전을 추가하고 캐싱을 추가한다.
GreetingControllerTest 클래스의 testCacheBustingOfStaticResources() 테스트 메서드를 통과시키면 된다.
