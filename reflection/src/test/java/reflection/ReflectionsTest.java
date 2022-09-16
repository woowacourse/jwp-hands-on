package reflection;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;
import java.lang.annotation.Annotation;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        printClasses(reflections, Controller.class);
        printClasses(reflections, Service.class);
        printClasses(reflections, Repository.class);
    }

    void printClasses(Reflections reflections, Class<? extends Annotation> classType) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(classType);
        classes.forEach(it -> log.info(it.toString()));
    }
}
