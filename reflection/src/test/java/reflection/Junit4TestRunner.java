package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        final Constructor<Junit4Test> constructor = clazz.getConstructor();
        final Junit4Test junit4Test = constructor.newInstance();

        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(MyTest.class)) { // isAnnotationPresent 이용
                declaredMethod.invoke(junit4Test);
            }
        }

//        for (Method declaredMethod : declaredMethods) {
//            final Annotation[] annotations = declaredMethod.getAnnotations();
//            Arrays.stream(annotations)
//                    .forEach(it -> System.out.println(it.annotationType().getName()));
//        }
    }
}
