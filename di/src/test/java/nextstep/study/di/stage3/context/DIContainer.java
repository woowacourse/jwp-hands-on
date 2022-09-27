package nextstep.study.di.stage3.context;

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

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
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
        final Field[] fields = bean.getClass().getDeclaredFields();
        for (final Field field : fields) {
            field.setAccessible(true);
            final Class<?> fieldType = field.getType();
            if (isNotInitializedField(bean, field)) {
                initializeField(bean, field, fieldType);
            }
        }
    }

    private boolean isNotInitializedField(final Object bean, final Field field) {
        try {
            return field.get(bean) == null;
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("fail to access field", e);
        }
    }

    private void initializeField(final Object bean, final Field field, final Class<?> fieldType) {
        for (final Object b : beans) {
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
