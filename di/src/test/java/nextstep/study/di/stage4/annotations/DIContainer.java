package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        final Set<Class<?>> classes = ClassPathScanner.getAllClassesInPackage(rootPackageName)
                .stream()
                .filter(DIContainer::filterServiceAndRepositoryAnnotation)
                .collect(Collectors.toSet());

        return new DIContainer(classes);
    }

    private static boolean filterServiceAndRepositoryAnnotation(final Class<?> aClass) {
        return aClass.isAnnotationPresent(Service.class) || aClass.isAnnotationPresent(Repository.class);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    private Object createInstance(final Class<?> beanClass) {
        try {
            final Constructor<?> constructor = beanClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException |
                       NoSuchMethodException e) {
            throw new RuntimeException("fail to create instance", e);
        }
    }

    private void setFields(final Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Inject.class))
                .forEach(it -> initializeField(bean, it));

    }

    private void initializeField(final Object bean, final Field field) {
        for (final Object b : beans) {
            field.setAccessible(true);
            final Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(b.getClass())) {
                try {
                    field.set(bean, b);
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(it -> aClass.isAssignableFrom(it.getClass()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("fail to find bean"));
    }
}
