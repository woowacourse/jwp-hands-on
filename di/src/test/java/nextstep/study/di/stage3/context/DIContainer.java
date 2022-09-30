package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) throws Exception {
        this.beans = registerBeans(classes);

        for (Object bean : beans) {
            setFields(bean);
        }
    }

    private Set<Object> registerBeans(final Set<Class<?>> classes) throws Exception {
        Set<Object> beans = new HashSet<>();
        for (Class<?> clazz : classes) {
            addBean(clazz, beans);
        }
        return beans;
    }

    private void addBean(final Class<?> clazz, final Set<Object> beans) throws Exception {
        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Object bean = declaredConstructor.newInstance();
        beans.add(bean);
    }

    private void setFields(final Object bean) throws Exception {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            fillField(field, bean);
        }
    }

    private void fillField(final Field field, final Object bean) throws Exception {
        if (field.get(bean) == null) {
            Object findBean = getBean(field.getType());
            field.set(bean, findBean);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T)beans.stream()
            .filter(clazz::isInstance)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}

