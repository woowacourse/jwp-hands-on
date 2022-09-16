package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test target = new Junit3Test();
        final Method[] methods = clazz.getDeclaredMethods();

        for (final Method method : methods) {
            final String methodName = method.getName();
            if (methodName.startsWith("test")) {
                method.invoke(target);
            }
        }
    }
}
