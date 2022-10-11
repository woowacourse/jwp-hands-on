package transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class FunctionWrapper {

    private static final Logger log = LoggerFactory.getLogger(FunctionWrapper.class);

    public static <T, R> Function<T, R> apply(ThrowingFunction<T, R, Exception> function) {
        return i -> {
            try {
                return function.apply(i);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        };
    }

    private FunctionWrapper() {}
}
