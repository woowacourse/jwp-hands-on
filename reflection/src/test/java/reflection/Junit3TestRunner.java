package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final var clazz = Junit3Test.class;
        final var instance = clazz.getDeclaredConstructor().newInstance();
        Stream.of(clazz.getDeclaredMethods())
                .filter(x -> x.getName().startsWith("test"))
                .forEach(method -> invoke(instance, method));
    }

    private static void invoke(final Junit3Test instance, final Method method) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
