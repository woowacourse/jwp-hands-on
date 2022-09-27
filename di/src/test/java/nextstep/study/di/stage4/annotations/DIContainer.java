package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    private DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        initialBeans();
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(this::createInstance)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Object createInstance(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    private void initialBeans() {
        for (Object bean : beans) {
            Field[] fields = bean.getClass().getDeclaredFields();
            initialFields(bean, fields);
        }
    }

    private void initialFields(final Object bean, final Field[] fields) {
        for (Field field : fields) {
            setField(bean, field);
        }
    }

    private void setField(final Object bean, final Field field) {
        try {
            if (hasInjectAnnotation(field)) {
                field.setAccessible(true);
                field.set(bean, getBean(field.getType()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasInjectAnnotation(final Field field) {
        return field.isAnnotationPresent(Inject.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        Object instance = beans.stream()
                .filter(aClass::isInstance)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 클래스 타입의 빈은 존재하지 않습니다. -> " + aClass));

        return (T) instance;
    }
}
