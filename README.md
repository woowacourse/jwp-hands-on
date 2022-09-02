# JWP Hands-On

## 만들면서 배우는 스프링 실습 코드

### 학습 순서
- cache
- thread
- servlet
- reflection
- di

## 요구사항
- [ ] 0단계 - 휴리스틱 캐싱 제거하기
- [ ] 1단계 - HTTP Compression 설정하기
- [ ] 2단계 - ETag/If-None-Match 적용하기
- [ ] 3단계 - 캐시 무효화(Cache Busting)


### 학습목표
- HTTP를 활용하여 사이트의 성능을 개선할 수 있다.
- HTTP 캐싱과 압축을 적용할 수 있다.


### 학습 순서
- 테스트 코드로 HTTP 헤더를 검증하는 방법을 익힌다.
- HTTP 압축을 설정한다.
- HTTP 헤더에 Cache-Control를 적용하고 캐싱이 적용됐는지 확인한다.
- 캐시 무효화(Cache Busting)를 학습한다.


### 실습 요구 사항
GreetingControllerTest 클래스의 모든 테스트 메서드를 통과시킨다.


### 0단계 - 휴리스틱 캐싱 제거하기
- HTTP 응답 헤더에 Cache-Control가 없어도 웹 브라우저가 휴리스틱 캐싱에 따른 암시적 캐싱을 한다.
- 의도하지 않은 캐싱을 막기 위해 모든 응답의 헤더에 Cache-Control: no-cache를 명시한다.
- 또한, 쿠키나 사용자 개인 정보 유출을 막기 위해 private도 추가한다.
- 모든 응답 헤더의 기본 캐싱을 아래와 같이 설정한다.
- `GreetingControllerTest 클래스의 testNoCachePrivate()` 테스트 메서드를 통과시키면 된다.
Cache-Control: no-cache, private

### 힌트
CacheWebConfig 클래스에서 Cache-Control 설정을 적용하면 된다.


### 2단계 - ETag/If-None-Match 적용하기
- ETag와 If-None-Match를 사용하여 HTTP 캐싱을 적용해보자.
- Spring mvc에서 ShallowEtagHeaderFilter 클래스를 제공한다.
- 필터를 사용하여 /etag 경로만 ETag를 적용하자.
- GreetingControllerTest 클래스의 testETag() 테스트 메서드를 통과시키면 된다.

### 힌트
EtagFilterConfiguration 클래스에서 ShallowEtagHeaderFilter 필터를 추가한다.


### 3단계 - 캐시 무효화(Cache Busting)
- 어떤 경우에 HTTP 캐시를 적용하는게 좋을까?
- JS, CSS 같은 정적 리소스 파일은 캐싱을 적용하기 좋다.
- 하지만 자바스크립트와 css는 개발이 진행됨에 따라 자주 변경되고, 자바스크립트와 CSS 리소스 버전이 동기화되지 않으면 화면이 깨진다.
- 캐시는 URL을 기반으로 리소스를 구분하므로 리소스가 업데이트될 때 URL을 변경하면 손쉽게 JS, CSS 파일의 캐시를 제거할 수 있다.
- 캐시 무효화는 콘텐츠가 변경될 때 URL을 변경하여 응답을 장기간 캐시하게 만드는 기술이다.
- 어떻게 적용할까?
- 정적 리소스 파일의 max-age를 최대치(1년)로 설정한다.
- ETag도 적용한다.
- JS, CSS 리소스에 변경사항이 생기면 캐시가 제거되도록 url에 버전을 적용하자.
- CacheBustingWebConfig 클래스의 addResourceHandlers 메서드의 registry에 리소스 디렉터리에 버전을 추가하고 캐싱을 추가한다.
- GreetingControllerTest 클래스의 testCacheBustingOfStaticResources() 테스트 메서드를 통과시키면 된다.
