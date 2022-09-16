package reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    /**
     * 메서드명 확인 & invoke로 실행시키기
     * - Junit3Test에서 test로 시작하는 메소드 실행
     */
    @Test
    void run() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        final var object = clazz.getDeclaredConstructor().newInstance();
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("test")) {
                method.invoke(object);
            }
        }
    }

    @Test
    void run_static_method_without_instance() throws Exception {
        final Class<Junit3Test> clazz = Junit3Test.class;
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                method.invoke(null);
            }
        }
    }
}
