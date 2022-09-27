package nextstep.study.di.stage4.annotations;

import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final var reflections = new Reflections(packageName);
        final var beanClasses = new HashSet<Class<?>>();
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Service.class));
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        beanClasses.addAll(reflections.getTypesAnnotatedWith(Inject.class));
        return beanClasses;
    }
}
