package concurrency.stage0;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스레드 풀은 무엇이고 어떻게 동작할까?
 * 테스트를 통과시키고 왜 해당 결과가 나왔는지 생각해보자.
 *
 * Thread Pools
 * https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html
 *
 * Introduction to Thread Pools in Java
 * https://www.baeldung.com/thread-pool-java-and-guava
 */
class ThreadPoolsTest {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolsTest.class);

    /**
     * `Executors.newFixedThreadPool(int nThreads)` : 무조건 일정 개수의 쓰레드만 생성해서 재사용.
     * - Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue.
     *
     * workQueue의 종류는 LinkedBlockingQueue
     * - 현재 생성된 쓰레드가 전부 사용 중이면 일단 대기 상태의 task로 관리
     */
    @Test
    void testNewFixedThreadPool() {
        final var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));

        final int expectedPoolSize = 2; // the current number of threads in the pool - 풀에 존재하는 thread 개수!
        final int expectedQueueSize = 1; // the task queue used by this executor - 현재 큐에서 대기 중인 task 개수!

        assertThat(expectedPoolSize).isEqualTo(executor.getPoolSize());
        assertThat(expectedQueueSize).isEqualTo(executor.getQueue().size());
    }

    /**
     * `Executors.newCachedThreadPool()` : 모든 작업들이 동시에 수행되는데 필요한 최대 개수의 쓰레드를 그때마다 생성하여 유지. 불필요해진 쓰레드는 제거.
     * - Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when they
     * are available.
     *
     * workQueue의 종류는 SynchronousQueue
     * - 태스크를 대기 상태로 관리하지 않고 그대로 새로운 쓰레드에 할당.
     * - 주의. 작업이 들어왔는데, 더 이상 생성할 수 있는 쓰레드가 없으면 예외 발생!
     */
    @Test
    void testNewCachedThreadPool() {
        final var executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.submit(logWithSleep("hello cached thread pools"));
        executor.submit(logWithSleep("hello cached thread pools"));
        executor.submit(logWithSleep("hello cached thread pools"));

        final int expectedPoolSize = 3;
        final int expectedQueueSize = 0;

        assertThat(expectedPoolSize).isEqualTo(executor.getPoolSize());
        assertThat(expectedQueueSize).isEqualTo(executor.getQueue().size());
    }

    /**
     * 실질적인 쓰레드 개수 : 최소 0, 최대 corePoolSize
     * - 초기에는 0개! task 들어오면서 필요할 때마다 쓰레드 생성!
     * - corePoolSize == 최대 쓰레드 개수! 다만, 필요에 따라 동적으로 수정!
     * - 쉬고 있는 쓰레드는 keepAliveTime만큼 대기한 이후에 제거!
     * 아무런 task도 들어오지 않으면 최종적으로는 다시 쓰레드 개수 0개로 유지!
     *
     * `ThreadPoolExecutor` 생성자의 매개변수 목록
     * - corePoolSize – the number of threads to keep in the pool, even if they are idle
     * - maximumPoolSize – the maximum number of threads to allow in the pool
     * - keepAliveTime – when the number of threads is greater than the core, this is the maximum time that excess idle
     * threads will wait for new tasks before terminating.
     * - unit – the time unit for the keepAliveTime argument
     * - workQueue – the queue to use for holding tasks before they are executed. This queue will hold only the Runnable
     * tasks submitted by the execute method.
     * - threadFactory – the factory to use when the executor creates a new threa
     */
    @Test
    void testCustomCachedThreadPool() throws InterruptedException {
        final var executor = new ThreadPoolExecutor(3, 100,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
        // new LinkedBlockingQueue<>(100): 쓰레드에 할당되지 못한 task 최대 100개가 큐에서 대기 상태일 수 있음

        Thread.sleep(100);
        // corePoolSize 개수에 맞춰 내부적으로 쓰레드 3개 생성되는 중. ThreadPoolExecutor 인스턴스 생성 시점에는 아직 0개.
        assertThat(executor.getPoolSize()).isEqualTo(0);
        assertThat(executor.getQueue().size()).isEqualTo(0);

        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));

        // 우선 생성되어있는 쓰레드 3개에만 작업이 할당되고, 나머지 2개는 태스크 큐에서 대기.
        assertThat(executor.getPoolSize()).isEqualTo(3);
        assertThat(executor.getQueue().size()).isEqualTo(2);

        // 대기 중인 task가 존재하면 쓰레드 개수 최대 10개까지 늘리도록 corePoolSize 수정.
        // 그러나 일단은 5개의 작업만 동시에 수행하면 되므로 5개까지만 쓰레드 생성.
        updateCorePoolSize(executor,10);
        assertThat(executor.getPoolSize()).isEqualTo(5);
        assertThat(executor.getQueue().size()).isEqualTo(0);
    }

    /**
     * queue에 대기 중인 task의 개수가 얼마나 되는지에 따라 corePoolSize를 동적으로 수정하여 자동으로 쓰레드풀의 크기 조절 가능!
     */
    private void updateCorePoolSize(ThreadPoolExecutor executor, int max) throws InterruptedException {
        if (executor.getQueue().size() > 0) {
            executor.setCorePoolSize(max);
            Thread.sleep(100);
        }
        if (executor.getQueue().size() == 0) {
            executor.setCorePoolSize(3);
            Thread.sleep(100);
        }
    }

    private Runnable logWithSleep(final String message) {
        return () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info(message);
        };
    }
}
