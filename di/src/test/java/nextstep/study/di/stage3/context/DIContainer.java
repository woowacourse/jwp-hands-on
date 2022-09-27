package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        initializeBeans();
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

    private void initializeBeans() {
        for (Object bean : beans) {
            Field[] fields = bean.getClass().getDeclaredFields();
            setFields(bean, fields);
        }
    }

    private void setFields(final Object bean, final Field[] fields) {
        for (Field field : fields) {
            setField(bean, field);
        }
    }

    private void setField(final Object bean, final Field field) {
        try {
            field.setAccessible(true);
            Object object = field.get(bean);
            if (Objects.isNull(object)) {
                field.set(bean, getBean(field.getType()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        Object instance = beans.stream()
                .filter(clazz::isInstance)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 클래스 타입의 빈은 존재하지 않습니다. -> " + clazz));

        return (T) instance;
    }
}
