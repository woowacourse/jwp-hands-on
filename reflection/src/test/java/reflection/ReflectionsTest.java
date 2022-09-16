package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    /**
     * 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
     */
    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");
        for (final var type : reflections.getTypesAnnotatedWith(Controller.class)) {
            log.info(type.toString());
        }
        for (final var type : reflections.getTypesAnnotatedWith(Service.class)) {
            log.info(type.toString());
        }
        for (final var type : reflections.getTypesAnnotatedWith(Repository.class)) {
            log.info(type.toString());
        }
    }
}
