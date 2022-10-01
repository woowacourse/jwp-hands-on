package nextstep.study.di.stage4.annotations;

import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class ClassPathScanner {

    public static Set<Class<?>> getAllClassesInPackage(final String packageName) {
        final Scanners scanners = Scanners.SubTypes.filterResultsBy(it -> true);
        final Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(packageName)
                .setScanners(scanners));
        return reflections.getSubTypesOf(Object.class);
    }
}
