## HTTP 캐시
1. HTTP Request 의 CacheControl `no-cache, private`를 처리할 수 있다.
   - 캐시가 유효한지 서버에 확인하기 위해 `no-cache`로 요청을 보낸다.
   - 서버에 캐시를 아예 저장하지 않는 `no-store`과는 다르다.
   - `Cache-Control` 정책은 기본으로 `private`이다.
   - `private`으로 하면 타인과 공유되는 프록시 서버에 캐시를 저장하지 않는다.
   - `Cache-Control`은 인터셉터, 혹은 컨트롤러에서 직접 response에 설정해줄 수 있다.
2. Http Compression 설정을 할 수 있다.
   - HTTP response 응답을 압축하여 성능을 높인다.
   - `gzip` 압축 알고리즘을 적용해 `Transfer-Encoding`을 `chunked`된 형태로 나누어 여러 번 보낸다.
