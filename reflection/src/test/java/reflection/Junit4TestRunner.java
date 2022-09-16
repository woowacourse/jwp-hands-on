package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test target = new Junit4Test();
        final Method[] methods = clazz.getDeclaredMethods();

        for (final Method method : methods) {
            final MyTest annotation = method.getDeclaredAnnotation(MyTest.class);
            if (annotation != null) {
                method.invoke(target);
            }
        }
    }
}
