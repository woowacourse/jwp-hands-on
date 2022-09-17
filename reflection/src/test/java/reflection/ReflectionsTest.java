package reflection;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() {
        Reflections reflections = new Reflections("examples");

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어있는 모든 클래스 찾아 로그로 출력한다.
        // Set<Class<?>> singletons = reflections.get(TypesAnnotated.with(Singleton.class).asClass());
        logClassesAnnotatedWith(reflections, Controller.class);
        logClassesAnnotatedWith(reflections, Service.class);
        logClassesAnnotatedWith(reflections, Repository.class);
    }

    private void logClassesAnnotatedWith(Reflections reflections, Class<? extends Annotation> annotation) {
        Set<Class<?>> classesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
        for (Class<?> clazz : classesAnnotatedWith) {
            log.info("the Class annotated with {}: {}", annotation.getName(), clazz.getName());
        }
    }
}
