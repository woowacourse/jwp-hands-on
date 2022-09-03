## HTTP 캐시란?

- HTTP는 client-server model, 요청에 대해 응답을 하는 구조. Application Layer (7계층).
- 클라이언트에서 특정 요청에 대한 서버의 응답을 저장했다가 재사용하는 것
    - 키: URL - 값: Response
    - `https://example.cpm/index.html` - `<!doctype html> ...`
- 클라이언트가 서버에 GET 요청을 보내지 않으므로 클라이언트의 성능은 향상, 서버의 부하는 감소!

## HTTP 캐시 종류

Browser[1] - Proxy[2] - ReverseProxy/CDN[3] - Server

- Private Cache: 웹브라우저의 캐쉬

    - 각 사용자의 웹브라우저에서 관리되므로 다른 사용자들은 접근 불가!
    - 사용자의 개인화된 응답 저장!
    - 주의. 서버의 응답에 `Authorization Header`가 있으면 Private Cache에 저장될 수 없음!

- Shared Cache (Proxy): 클라이언트와 서버 사이의 중개자

    - 개발자들이 직접 제어 불가!
    - HTTP 헤더를 통해 특정 방식으로 캐쉬를 처리하도록 알려주는 것은 가능!

- **Shared Cache (Managed)**: Reverse Proxy 혹은 CDN!
    - Reverse Proxy: Nginx의 캐쉬 설정 가능!
    - CDN: AWS CloudFront, AWS CloudFlare 등 업체의 대쉬보드 사이트에서 캐쉬 관련 설정으로 동작 제어 가능!
    - 개발자들이 제어 가능!

`Cache-Control: public, ~`인 경우 Shared Cache 사용 가능!
