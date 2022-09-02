# Cache

[📖 HTTP 활용하기 (woowahan.com)](https://techcourse.woowahan.com/s/cCM7rQR9/ls/cxmbDHT7)


### 0단계 - 휴리스틱 캐싱 제거하기
- [ ] 모든 응답의 `Cache-Control` header 값을 `no-cache, private`로 설정한다.

### 1단계 - HTTP Compression 설정하기
- [ ] 모든 응답에 `gzip` 압축 알고리즘을 적용한다.

### 2단계 - ETag/If-None-Match 적용하기
- [ ] `/etag` 요청에 대한 응답 헤더에 `ETag`와 `If-Non-Match`를 적용한다.

### 3단계 - 캐시 무효화(Cache Busting)
- [ ] 정적 리소스 응답 시 `max-age`를 1년으로 설정한다.
- [ ] 정적 리소스 응답 시 `ETag`를 적용한다.
- [ ] url에 버전을 적용해 변경 사항이 있을 경우 캐시를 무효화한다.
