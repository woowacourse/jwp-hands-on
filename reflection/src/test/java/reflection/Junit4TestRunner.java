package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        final Junit4Test junit4Test = new Junit4Test();
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행

        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            final MyTest annotation = method.getAnnotation(MyTest.class);
            if (annotation != null) {
                method.invoke(junit4Test);
            }
        }
    }
}
