package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = createBeans(classes);
        this.beans.forEach(this::injectFields);
    }

    private Set<Object> createBeans(final Set<Class<?>> classes) throws Exception {
        final Set<Object> beans = new HashSet<>();
        for (final Class<?> aClass : classes) {
            final Constructor<?> aClassConstructor = aClass.getDeclaredConstructor();
            aClassConstructor.setAccessible(true);
            final Object newInstance = aClassConstructor.newInstance();
            beans.add(newInstance);
        }

        return beans;
    }

    private void injectFields(final Object bean) {
        final Class<?> aClass = bean.getClass();
        final Field[] fields = aClass.getDeclaredFields();
        Arrays.stream(fields)
                .forEach(field -> setField(bean, field));
    }

    private <T> void setField(final T instance, final Field field) {
        try {
            for (final Object bean : beans) {
                if (field.getType().isAssignableFrom(bean.getClass())) {
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }
        } catch (final IllegalAccessException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) getBeanInstance(aClass);
    }

    private Object getBeanInstance(final Class<?> aClass) {
        return beans.stream()
                .filter(bean -> bean.getClass() == aClass)
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }
}
