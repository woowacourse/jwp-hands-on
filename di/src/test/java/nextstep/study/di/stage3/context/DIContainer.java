package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = new HashSet<>();
        initialize(classes);
    }

    private void initialize(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (isNotBean(clazz)) {
                instantiateBean(clazz);
            }
        }
    }

    private Object instantiateBean(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        if (parameterTypes.length == 0) {
            Object instance = instantiate(clazz);
            beans.add(instance);
            return instance;
        }

        Object bean = instantiateConstructor(clazz);
        beans.add(bean);
        return bean;
    }

    private Object instantiateConstructor(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        ArrayList<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            Object parameterBean = instantiateBean(parameterType);
            parameters.add(parameterBean);
        }
        return instantiate(clazz, parameters);
    }

    private Object instantiate(Class<?> clazz, Object... parameters) {
        try {
            return getConstructor(clazz).newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
//        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0 && clazz.isInterface()) {
            Reflections reflections = new Reflections();
            Set<Class<?>> subTypesOf = reflections.getSubTypesOf(clazz);
        }
            return constructors[0];
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
    }

    private boolean isNotBean(Class<?> clazz) {
        return beans.stream()
                .noneMatch(clazz::isInstance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow();
    }
}
