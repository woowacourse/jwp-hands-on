package reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    /**
     * 어노테이션이 붙은 메서드 찾기
     * - Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
     */
    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final var object = clazz.getDeclaredConstructor().newInstance();
        for (Method method : clazz.getMethods()) {
            for (final var annotation : method.getAnnotations()) {
                if (annotation.annotationType().equals(MyTest.class)) {
                    method.invoke(object);
                }
            }
        }
    }

    @Test
    void run_instance_to_class() throws Exception {
        Junit4Test object = new Junit4Test();
        final var clazz = object.getClass();
        for (Method method : clazz.getMethods()) {
            for (final var annotation : method.getAnnotations()) {
                if (annotation.annotationType().equals(MyTest.class)) {
                    method.invoke(object);
                }
            }
        }
    }
}
