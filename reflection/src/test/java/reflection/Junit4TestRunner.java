package reflection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final var clazz = Junit4Test.class;
        final var instance = clazz.getDeclaredConstructor().newInstance();
        Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invoke(instance, method));
    }

    private static void invoke(final Junit4Test instance, final Method method) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
