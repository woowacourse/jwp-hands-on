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

    @Test
    void showAnnotationClass() {
        final Reflections reflections = new Reflections("examples");

        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(it -> log.info(it.getName()));

        reflections.getTypesAnnotatedWith(Service.class)
                .forEach(it -> log.info(it.getName()));

        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(it -> log.info(it.getName()));

    }
}
