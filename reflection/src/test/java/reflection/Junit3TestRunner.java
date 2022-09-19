package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        List<Method> test = Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> it.getName().startsWith("test"))
                .collect(Collectors.toList());

        Junit3Test junit3Test = new Junit3Test();
        for (Method method : test) {
            method.invoke(junit3Test);
        }
    }
}
