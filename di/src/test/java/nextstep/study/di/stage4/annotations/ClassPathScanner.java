package nextstep.study.di.stage4.annotations;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final var reflections = new Reflections(packageName, Scanners.SubTypes.filterResultsBy(filter -> true));
        return reflections.getSubTypesOf(Object.class);
    }
}
