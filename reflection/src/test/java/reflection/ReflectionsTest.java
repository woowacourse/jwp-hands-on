package reflection;

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
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        Set<Class<?>> clazz = reflections.getTypesAnnotatedWith(Controller.class);
        clazz.addAll(reflections.getTypesAnnotatedWith(Service.class));
        clazz.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        for (Class<?> aClass : clazz) {
            log.info(String.valueOf(aClass));
        }
    }
}
