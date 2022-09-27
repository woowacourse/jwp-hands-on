package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        for (final var bean : beans) {
            setFields(bean);
        }
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return new DIContainer(ClassPathScanner.getAllClassesInPackage(rootPackageName));
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) {
        return classes.stream()
                .map(Class::getDeclaredConstructors)
                .flatMap(Arrays::stream)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .map(this::instantiateEmptyBean)
                .collect(Collectors.toSet());
    }

    private void setFields(final Object bean) {
        for (final var field : bean.getClass().getDeclaredFields()) {
            for (final var dependency : this.beans) {
                if (field.getType().isAssignableFrom(dependency.getClass())) {
                    setField(bean, field, dependency);
                }
            }
        }
    }

    private void setField(Object bean, Field field, Object dependency) {
        try {
            field.setAccessible(true);
            field.set(bean, dependency);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private Object instantiateEmptyBean(Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        for (final var bean : beans) {
            if (clazz.isAssignableFrom(bean.getClass())) {
                return (T) bean;
            }
        }
        return null;
    }
}
