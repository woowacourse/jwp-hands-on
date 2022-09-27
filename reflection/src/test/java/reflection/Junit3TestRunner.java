package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        // 타입을 통해 획득
        Class<Junit3Test> clazz = Junit3Test.class;
        //  인스턴스를 통해 획득
        final Junit3Test junit3Test1 = new Junit3Test();
        final Class<? extends Junit3Test> aClass = junit3Test1.getClass();

        // FQCN을 이요애 획득
        final Class<?> aClass1 = Class.forName("reflection.Junit3Test");

        // TODO Junit3Test에서 test로 시작하는 메소드 실행

        // 인스턴스 생성 후 실행 방법
        final Constructor<Junit3Test> constructor = clazz.getConstructor();
        final Junit3Test junit3Test = constructor.newInstance();
        junit3Test.test1();
        junit3Test.test2();
        junit3Test.three();

        // Method 객체 이용 방법
        final Method test1 = clazz.getDeclaredMethod("test1");
        test1.invoke(junit3Test);
        final Method test2 = clazz.getDeclaredMethod("test2");
        test2.invoke(junit3Test);
        final Method three = clazz.getDeclaredMethod("three");
        three.invoke(junit3Test);

        // getDeclaredMethods()
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            declaredMethod.invoke(junit3Test);
        }
    }
}
