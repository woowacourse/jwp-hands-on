## The HTTP Connector

`HTTP Connector`: Catalina가 독립된 웹서버로 동작할 수 있도록 해주는 Connector 컴포넌트

- HTTP1/1 프로토콜!
- servlet, JSP 페이지 실행 가능!
- 특정 TCP 포트에 대한 커넥션을 listen함.
- 하나의 Service에 대해 하나 이상의 `Connector` 설정 가능!

## 쓰레드와 커넥션 처리

1. 개별 요청은 하나의 쓰레드에서 처리되어야 함. 요청 처리를 위해 만들어진 쓰레드의 현재 개수보다 더 많은 양의 요청들이 들어온 경우, 추가적인 쓰레드들이 생성된다. (쓰레드의 최대 개수는 `maxThreads` 설정 값)

2. 그보다도 요청이 계속 들어오면 톰캣에서는 `maxConnections`에 도달할 때까지만 커넥션을 추가로 받아들인다.

- max-connections 제대로 이해하려면 NIO 개념 필요

3. 커넥션들은 idle 쓰레드가 생길 때까지 `Connector`에 의해 만들어진 server socket 내부에 큐(FIFO)로 관리됨!

4. `maxConnections`에 도달하면 추가적인 커넥션들은 운영체제에 의해 큐로 관리됨! 운영체제에 의해 제공되는 `커넥션 큐`는 `acceptCount` 속성 값을 통해 제어될 수도 있음.

5. OS의 큐도 차게 된다면 그 이상의 커넥션들은 거절되거나 타임아웃될 수 있음.

[공식문서](https://tomcat.apache.org/tomcat-8.5-doc/config/http.html)

### 핵심 속성들

- `minSpareThreads`: 지속적으로 실행 중인 쓰레드의 최소 개수. active & idle 쓰레드. 디폴트 값은 10.

    - `server.tomcat.threads.min-spare`

- `maxThreads`: Connector에 의해 생성되는 요청을 처리하는 쓰레드의 최대 개수. 즉, **동시에 처리될 수 있는 요청의 최대 개수**. 디폴트 값은 `200`.

    - `server.tomcat.threads.max`
    - 주의. 해당 connector에 executor가 연결된 경우, 해당 설정은 무시되고 executor을 활용하여 task를 실행함.

- `maxConnections`: **톰캣 서버 커넥션 풀의 크기**. 해당 서버가 동시에 받아들이고 처리할 커넥션의 최대 개수.

    - `server.tomcat.max-connections`
    - 해당 값을 초과하여 들어온 커넥션을 서버에서는 받아들이기는 하지만 즉시 처리하지는 않음!
    - For NIO and NIO2 the default is `10000`. For APR/native, the default is `8192`.

- `acceptCount`: maxConnections 값을 초과하여 들어온 커넥션에 대해 운영체제에서 관리해주는 커넥션 큐의 크기! 디폴트 값은 100.
    - `server.tomcat.accept-count`
    - OS는 해당 설정을 무시하고 다른 값을 커넥션 큐의 크기로 사용 가능.
    - 해당 큐까지 차게 되었을 때 들어오는 요청들은 OS에 의해 거절하거나, timeout될 수 있음.

---

## 부록

### Common Attributes

`Connector` 인터페이스의 모든 구현체들의 공통 속성들

- `allowTrace`: HTTP 메서드 중 TRACE는 처리하지 않는 것이 디폴트 값.
- `asyncTimeout` : 비동기 요청이 얼마나 오랫동안 처리되지 않으면 timeout되는가. 디폴트 값은 30초.
- `discardFacades`
- `enableLookups`
- `encodedSolidusHandling`
- `maxCookieCount`: 요청에 존재할 수 있는 쿠키의 최대 개수. 디폴트 값은 200.
- `maxParameterCount`: 최대 몇 개의 params를 읽고 사용할 것인가. 이 값에 해당되는 개수만큼만 처리. 디폴트 값은 10000.
- `maxPostSize`: The maximum size in bytes of the POST which will be handled by the container FORM URL parameter parsing. ...
- `maxSavePostSize`: The maximum size in bytes of the request body which will be saved/buffered by the container during FORM or CLIENT-CERT authentication or during HTTP/1.1 upgrade. ...
- `parseBodyMethods`: 요청 메시지의 바디가 `application/x-www-form-urlencoded`인 경우, key-value 쌍의 파라미터를 읽어들일 대상 HTTP 메서드 목록. 디폴트는 POST만!
- `port`: The TCP port number on which this Connector will create a server socket and await incoming connections.
- `protocol`: 들어오는 트래픽을 처리할 때 사용할 프로토콜. 디폴트는 `HTTP/1.1`
- `proxyName`
- `proxyPort`
- `redirectPort`
- `scheme`: 디폴트는 `http`. SSL Connector에 대해 `https` 설정.
- `secure`
- `URIEncoding`: URI 바이트를 디코드할 때 사용할 인코딩. `org.apache.catalina.STRICT_SERVLET_COMPLIANCE` 시스템 속성 값이 `true`면 `ISO-8859-1`를 사용. 그 외의 경우 UTF-8 사용.
- `useBodyEncodingForURI`
- `useIPVHosts`
- `xpoweredBy`

### Standard Implementation

표준 HTTP Connector(NIO, NIO2, APR/native)들 모두 다음 attributes를 지원함.

- **`acceptCount`: maxConnections를 초과한 커넥션에 대해 운영체제에서 관리해주는 커넥션 큐의 크기!**

    - The maximum length of the **operating system provided queue** for incoming connection requests **when maxConnections has been reached**. The operating system may ignore this setting and use a different size for the queue. When this queue is full, the operating system may actively refuse additional connections or those connections may time out. The default value is 100.

- `acceptorThreadPriority`: The priority of the acceptor thread. The _thread used to accept new connections_. The default value is 5 ...
- `address`
- `allowHostHeaderMismatch`
- `allowedTrailerHeaders`
- `bindOnInit`
- `clientCertProvider`
- `compressibleMimeType`
- `compression`
- `compressionMinSize`
- `connectionLinger`
- `connectionTimeout`: 커넥션이 생성된 이후 요청의 첫 줄이 도달할 때까지 대기하는 기간. 디폴트 값은 `60000`(60초), `20000`(20초). 요청의 바디 메시지를 읽을 때에 사용될 수도 있음.
- `connectionUploadTimeout`: 데이터 업로드 기간 동안의 timeout 설정

- `continueResponseTiming`: `Expect: 100-continue` 헤더가 포함된 요청에 대해 언제 100을 응답해야 하는가?

    - `immediately`: 즉시 100 응답
    - `onRead`: 서블릿에서 request body를 읽을 경우에만 응답. 즉, 서블릿에서 헤더를 분석하고 request body도 요청하고자 할 경우 상태코드 100 응답하도록 함.

- `defaultSSLHostConfigName`
- `disableUploadTimeout`
- `executor`: A reference to the name in an Executor element. If this attribute is set, and the named executor exists, the connector will use the executor, and all the other thread attributes will be ignored. Note that if a shared executor is not specified for a connector then the connector will use a private, internal executor to provide the thread pool.

- `executorTerminationTimeoutMillis`
- **`keepAliveTimeout`: The number of milliseconds this Connector will wait for another HTTP request before closing the connection.** 디폴트 값은 `connectionTimeout` 속성 값.

- **`maxConnections`: The maximum number of connections that the server will accept and process at any given time.** When this number has been reached, **the server will accept, but not process, one further connection.** This additional connection be blocked until the number of connections being processed falls below maxConnections at which point the server will start accepting and processing new connections again. Note that **once the limit has been reached, the operating system may still accept connections based on the `acceptCount` setting.** The default value varies by connector type. For NIO and NIO2 the default is `10000`. For APR/native, the default is `8192`.

- `maxExtensionSize`
- `maxHeaderCount`
- `maxHttpHeaderSize`
- `maxHttpRequestHeaderSize`
- `maxHttpResponseHeaderSize`
- `maxKeepAliveRequests`: 서버에 의해 커넥션이 닫힐 때까지 파이프라인될 수 있는 HTTP 요청의 최대 개수. 1로 설정하면 keep-alive를 무효처리됨. 디폴트 값은 `100`.

- `maxSwallowSize`: The maximum number of request body bytes (excluding transfer encoding overhead) that will be swallowed by Tomcat for an aborted upload. An aborted upload is when Tomcat knows that the request body is going to be ignored but the client still sends it. If Tomcat does not swallow the body the client is unlikely to see the response. If not specified the default of 2097152 (2 megabytes) will be used. A value of less than zero indicates that no limit should be enforced.

- **`maxThreads`: The maximum number of request processing threads to be created by this Connector, which therefore determines the maximum number of simultaneous requests that can be handled**. If not specified, this attribute is set to `200`. If an executor is associated with this connector, this attribute is ignored as the connector will execute tasks using the executor rather than an internal thread pool. Note that if an executor is configured any value set for this attribute will be recorded correctly but it will be reported (e.g. via JMX) as -1 to make clear that it is not used.

- `maxTrailerSize`: Limits the total length of trailing headers in the last chunk of a chunked HTTP request. If the value is -1, no limit will be imposed. If not specified, the default value of 8192 will be used.

- **`minSpareThreads`: 지속적으로 실행 중인 쓰레드의 최소 개수. active & idle 쓰레드. 디폴트 값은 10.**
- `noCompressionStrongETag`: This flag configures whether resources with a strong ETag will be considered for compression. **If true, resources with a strong ETag will not be compressed**. The default value is true. / This attribute is deprecated. It will be removed in Tomcat 10 onwards where it will be hard-coded to true.
- `noCompressionUserAgents`
- `processorCache`
- `rejectIllegalHeader`: 요청 메시지에 잘못된 헤더가 존재하는 경우 즉시 400을 반환할지(true) 아니면 해당 헤더 값만 무시할 것인가(false). 디폴트 값은 false.
- `relaxedPathChars`
- `relaxedQueryChars`
- `restrictedUserAgents`
- `sendReasonPhrase`: Set this attribute to true if you wish to have a reason phrase in the response. The default value is false.
- `server`
- `serverRemoveAppProvidedValues`
- `SSLEnabled`
- `tcpNoDelay`
- `threadPriority`: The priority of the request processing threads within the JVM. The default value is 5 (the value of the java.lang.Thread.NORM_PRIORITY constant). ...
- `useKeepAliveResponseHeader`
