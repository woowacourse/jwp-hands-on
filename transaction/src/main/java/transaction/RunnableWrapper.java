package transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RunnableWrapper {

    private static final Logger log = LoggerFactory.getLogger(RunnableWrapper.class);

    public static Runnable accept(ThrowingRunnable<Exception> runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        };
    }

    private RunnableWrapper() {}
}
