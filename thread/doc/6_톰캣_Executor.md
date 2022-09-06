## The Executor (thread pool)

`Executor` : Connector 등 톰캣 내 컴포넌트들 사이에서 공유되는 쓰레드풀.

- `org.apache.catalina.Executor` 인터페이스의 구현체
- 과거에는 Connector 1개당 쓰레드풀이 있었으나 Connector 이외의 컴포넌트들에서도 사용 가능!
- **Connector들에 의해 pick up되어야 하는 대상**! 이를 위해 Connector보다 먼저 server.xml에 나타나야 함!

[공식문서](https://tomcat.apache.org/tomcat-8.5-doc/config/executor.html)

### Attributes

Executor의 디폴트 구현체의 속성들

`threadPriority`
(int) The thread priority for threads in the executor, the default is 5 (the value of the Thread.NORM_PRIORITY constant)

`daemon`
(boolean) Whether the threads should be daemon threads or not, the default is `true`

`namePrefix`
(String) The name prefix for each thread created by the executor. The thread name for an individual thread will be namePrefix+threadNumber

`maxThreads`
(int) The max number of active threads in this pool, default is `200`

`minSpareThreads`
(int) The minimum number of threads (idle and active) always kept alive, default is `25`

`maxIdleTime`
(int) The number of milliseconds before an idle thread shutsdown, unless the number of active threads are less or equal to minSpareThreads. Default value is 60000(1 minute)

`maxQueueSize`
(int) The maximum number of runnable tasks that can queue up awaiting execution before we reject them. Default value is Integer.MAX_VALUE

`threadRenewalDelay`
(long) If a ThreadLocalLeakPreventionListener is configured, it will notify this executor about stopped contexts. After a context is stopped, threads in the pool are renewed. To avoid renewing all threads at the same time, this option sets a delay between renewal of any 2 threads. The value is in ms, default value is 1000 ms. If value is negative, threads are not renewed.
