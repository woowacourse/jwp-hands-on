- HTTP/1.1 공식 문서는?

→ RFC 2616 ([https://www.rfc-editor.org/rfc/rfc2616](https://www.rfc-editor.org/rfc/rfc2616))

### HTTP 특징

- stateless
- status : 200, 201…
- http method
- client-server model

### HTTP 캐싱 키워드

- cache-control
- etag
- 재검증

---

### HTTP 캐시에 대해 알아보자!

# HTTP 캐시란?

특정 요청의 응답을 저장했다가 재사용하는 기술

- 응답을 재사용하면 클라이언트가 서버에 요청을 전달할 필요가 없다.
- 클라이언트가 빠르게 응답을 받을 수 있다.
- 캐시 저장 기준은 URL을 기준으로 저장한다. (GET 메소드를 캐싱할 수 있다)

# HTTP 캐시 종류(3가지가 있다)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f543700b-4c5e-4933-ae90-dbee5fd23114/Untitled.png)

### 1. Private Cache

- 다른 사용자 접근 불가
- 사용자의 개인화된 정보

### 2. Shared Cache (Proxy)

- 중계자 역할 (클라이언트/서버)
- 직접 제어 불가능

### 3. Shared Cache (Managed)

- 제어 가능
    - Reverse Proxy : nginx
    - CDN : AWS 클라우드 프론트


# 캐시 유효기간

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e00f52c5-4360-47f6-8a17-c2d7fbe43037/Untitled.png)

- Date: 응답을 한 시간
- Cache-Control: 생성한 시점부터 캐싱을 유효하는 기간. 초 단위

캐시의 상태는 어떤 것이 있을까?

- fresh: 최신, 유효하다.
- stale: 오래된, 만료되다.



# 재검증(= 유효성검증, revalidation)

### 재검증이 무엇일까?

유효기간이 지난 캐시를 바로삭제할까? 물론 아니다. 계속 써도 되는지 서버에 확인하는 과정을 거치는데 이를 재검증이라 한다.

### 재검증 하는 방법

재검을 하는 방법에 크게 3가지가 있다. If-Modified-Since, ETag/If-None-Match, Force Revalidation 이 존재한다. 하나씩 살펴보자!

### 1. If-Modified-Since: 지난 날짜를 통해 검증 (초단위만 가능)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ed758d29-04a5-4c94-a702-3fb2018d0835/Untitled.png)

- (처음) 서버가 클라이언트의 요청받은 응답을 보낼 때 Last-Modified에 생성 시점을 보낸다.
- (1시간 이후) 클라이언트(브라우저)가 서버에 If-Modified-Since 헤더에 Last-Modified 값을 넣어서 보낸다.
- 서버에서 검증한다.
    - 200: 갱신한다.
    - 304: 갱신하지 않는다. (반환값이 없다)

### 2. ETag/If-None-Match: 태크 값을 받아 재검증해야 하는지 검증

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/802aa3f9-fe22-44cc-85e0-a86365e7803f/Untitled.png)

- (처음) 서버가 클라이언트의 요청받은 응답을 보낼 때 ETag에 해시값을 넣어 보낸다.
- 클라이언트(브라우저)가 서버에 If-None-Match 헤더에 ETag 값을 넣어 보낸다.
- 서버에서 검증한다.
    - 200: 갱신한다.
    - 304: 갱신하지 않는다. (반환값이 없다)

### 3. Force Revalidation: 항상 최신화한다 (항상 검증)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d320144f-43cc-40d3-893f-0d9b1a4c4ff4/Untitled.png)

- 서버가 클라이언트에 응답을 보낼 때, Cache-Control에 no-cache를 추가한다.

# 일반적인 캐싱 패턴을 살펴보자

### 1. 기본 설정은 no-cache로 설정하자.

```java
Cache-Control: no-cache
```

- Force Revalidation이다.
- 설정을 해주지 않으면 휴리스틱 캐싱이 발생할 수 있다.
    - 휴리스틱 캐싱: 의도하지 않은 캐싱(브라우저, 프록시)
- 콘텐츠에 개인화 된 정보가 있을 수 있다면 다음과 같이 private를 추가한다.

```java
Cache-Control: no-cache, private
```

### 2. 정적파일을 url로 등록하자. (feat. 캐시무효화)

그렇다면, 캐싱을 어디에 적용을 할까? 정적 리소스(js, css)에 하는 것이 좋다. 왜냐하면, 수정사항을 새로 배포하지 않는다면, 값이 변경되지 않기 때문이다.

캐시를 할 때 `캐시의 키`는 위에서 말한 것처럼 `url`이다. 정적파일을 url로 등록하여 캐시한다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/280d39d1-45c9-4356-a82d-bec161a89bf1/Untitled.png)

- (배포시) 기존 url에 버전이나 해시값을 추가하면 기존 캐시가 삭제되지 않는다. `(캐시 무효화)`
- 버전을 쓰면 기존 값을 기억해야 하기 때문에 해시값을 사용하는 방법도 있다.

### 3. 재검증은 ETag를 사용하자

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2ffa7f46-27f4-4271-9abf-20b7db348c97/Untitled.png)

- immutable을 지원하지 않는 서버도 있어 필수는 아니다.
- Last-Modified와 ETag를 같이쓰면 ETag가 적용된다.

### 4. Main resources는 캐싱이 불가능하다.

항상 최신의 상태로 만들기 위해 `no-cache, private`을 적용한다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ea49b995-8b6e-4dc3-bc62-bb565b0921b4/Untitled.png)

- 캐싱이 가능하다면 ETag를 설정한다.

---

# 실습을 해보자

## ****0단계 - 휴리스틱 캐싱 제거하기****

- HTTP 응답 헤더에 **`Cache-Control`**가 없어도 웹 브라우저가 **`휴리스틱 캐싱`**에 따른 암시적 캐싱을 한다.
- 의도하지 않은 캐싱을 막기 위해 모든 응답의 헤더에 **`Cache-Control: no-cache`**를 명시한다.
- 또한, 쿠키나 사용자 개인 정보 유출을 막기 위해 **`private`**도 추가한다.

```java
@Component
public class CacheInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String cacheControl = CacheControl
                .noCache()
                .cachePrivate()
                .getHeaderValue();
        response.addHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
        return true;
    }
}
```

```java
@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

    private final CacheInterceptor cacheInterceptor;

    public CacheWebConfig(final CacheInterceptor cacheInterceptor) {
        this.cacheInterceptor = cacheInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(cacheInterceptor);
    }
}
```

## ****1단계 - HTTP Compression 설정하기****

- HTTP 응답을 압축하면 웹 사이트의 성능을 높일 수 있다.
- 스프링 부트 설정을 통해 **`gzip`**과 같은 HTTP 압축 알고리즘을 적용시킬 수 있다.
- 테스트 코드가 통과하는지
- **`gzip`**이 적용됐는지 테스트 코드가 아닌 웹 브라우저에서 HTTP 응답의 헤더를 직접 확인한다.

```java
server:
  compression:
    enabled: true
    min-response-size: 10
```

- min-response-size 기본 2KB인데 10으로 늘려 해결할 수 있다!

## ****2단계 - ETag/If-None-Match 적용하기****

- **`ETag`**와 **`If-None-Match`**를 사용하여 HTTP 캐싱을 적용해보자.
- Spring mvc에서 **`ShallowEtagHeaderFilter`** 클래스를 제공한다.

### 2-1. 모든 응답에 etag 붙이기

```java
@Bean
public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
    return new ShallowEtagHeaderFilter();
}
```

### 2-2. 등록된 url에 맞는 etag 붙이기

```java
@Bean
public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
    FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean
      = new FilterRegistrationBean<>( new ShallowEtagHeaderFilter());
    filterRegistrationBean.addUrlPatterns("/etag");
    return filterRegistrationBean;
}
```

## 3. ****3단계 - 캐시 무효화(Cache Busting)****

- JS, CSS 같은 정적 리소스 파일은 캐싱을 적용하기 좋다.
- 하지만 자바스크립트와 css는 개발이 진행됨에 따라 자주 변경되고, 자바스크립트와 CSS 리소스 버전이 동기화되지 않으면 화면이 깨진다.
- 캐시는 URL을 기반으로 리소스를 구분하므로 리소스가 업데이트될 때 URL을 변경하면 손쉽게 JS, CSS 파일의 캐시를 제거할 수 있다.
- 캐시 무효화는 콘텐츠가 변경될 때 URL을 변경하여 응답을 장기간 캐시하게 만드는 기술이다.

```java
@Configuration
public class CacheBustingWebConfig implements WebMvcConfigurer {

    public static final String PREFIX_STATIC_RESOURCES = "/resources";

    private final ResourceVersion version;

    @Autowired
    public CacheBustingWebConfig(ResourceVersion version) {
        this.version = version;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(PREFIX_STATIC_RESOURCES + "/" + version.getVersion() + "/**")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic())
                .addResourceLocations("classpath:/static/");
    }
}
```

```java
@Configuration
public class EtagFilterConfiguration {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean
                = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/etag", "/resources/*");
        return filterRegistrationBean;
    }
}
```