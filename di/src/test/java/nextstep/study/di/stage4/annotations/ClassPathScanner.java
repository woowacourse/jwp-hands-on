package nextstep.study.di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> repositories = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);

        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(repositories);
        classes.addAll(services);
        return classes;
    }
}
