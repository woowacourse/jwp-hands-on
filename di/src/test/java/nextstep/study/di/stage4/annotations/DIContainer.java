package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private static final Logger log = LoggerFactory.getLogger(DIContainer.class);

    private final Set<Object> beans;

    private DIContainer(final Set<Class<?>> classes) {
        final Set<Object> beanInstances = classes.stream()
                .map(this::instantiateBean)
                .collect(Collectors.toSet());
        this.beans = toInjectedBeans(beanInstances);
    }

    private Object instantiateBean(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final Exception e) {
            log.warn("빈 생성 실패 {}", e);
            return null;
        }
    }

    private Set<Object> toInjectedBeans(final Set<Object> beanInstances) {
        return beanInstances.stream()
                .map(it -> toInjectedBean(beanInstances, it))
                .collect(Collectors.toSet());
    }

    private Object toInjectedBean(final Set<Object> beanInstances, final Object beanInstance) {
        final Field[] fields = beanInstance.getClass()
                .getDeclaredFields();
        for (final Field field : fields) {
            final Optional<Object> injectableBean = findInjectableBean(beanInstances, field);
            injectableBean.ifPresent(it -> {
                log.info("Inject {} to {}", it.getClass(), beanInstance.getClass());
                injectBeanToField(beanInstance, field, it);
            });
        }
        return beanInstance;
    }

    private Optional<Object> findInjectableBean(final Set<Object> beanInstances, final Field field) {
        if (field.getDeclaredAnnotation(Inject.class) == null) {
            return Optional.empty();
        }
        return beanInstances.stream()
                .filter(it -> field.getType().isInstance(it))
                .findFirst();
    }

    private void injectBeanToField(final Object beanInstance, final Field field, final Object fieldValueBean) {
        try {
            field.setAccessible(true);
            field.set(beanInstance, fieldValueBean);
        } catch (final Exception e) {
            log.warn("빈 의존성 주입 실패 {}", e);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> annotatedClasses = ClassPathScanner.getAllClassesInPackage(rootPackageName);
        return new DIContainer(annotatedClasses);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow();
    }
}
