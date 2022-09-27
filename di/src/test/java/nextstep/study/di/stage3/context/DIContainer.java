package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final HashMap<Class<?>, Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        HashMap<Class<?>, Object> beans = new HashMap<>();
        initNoDependencyBeans(classes, beans);
        initBeansWithConstructorDependencies(classes, beans);
        if (!classes.isEmpty()) {
            throw new IllegalArgumentException("빈 생성에 실패하였습니다.");
        }
        this.beans = beans;
    }

    private void initBeansWithConstructorDependencies(Set<Class<?>> classes, HashMap<Class<?>, Object> beans) {
        boolean stopConfig = beans.isEmpty();
        while (!stopConfig) {
            stopConfig = true;
            for (final var clazz : classes) {
                for (final var constructor : clazz.getConstructors()) {
                    Class<?>[] neededParameterTypes = constructor.getParameterTypes();
                    final var validParameters = getMatchingDependencies(beans, neededParameterTypes);
                    if (validParameters.size() == neededParameterTypes.length) {
                        beans.put(clazz, initBean(constructor, validParameters.toArray()));
                        classes.remove(clazz);
                        stopConfig = false;
                    }
                }
            }
        }
    }

    private List<Object> getMatchingDependencies(HashMap<Class<?>, Object> beans, Class<?>[] neededParameterTypes) {
        final var validParameters = new ArrayList<>();
        for (final var bean : beans.values()) {
            for (final var parameterType : neededParameterTypes) {
                if (parameterType.isAssignableFrom(bean.getClass())) {
                    validParameters.add(bean);
                }
            }
        }
        return validParameters;
    }

    private void initNoDependencyBeans(Set<Class<?>> classes, HashMap<Class<?>, Object> beans) {
        final var noDependencyBeans = initNoDependencyBeans(classes);
        for (final var bean : noDependencyBeans) {
            final var clazz = bean.getClass();
            beans.put(clazz, bean);
            classes.remove(clazz);
        }
    }

    private List<Object> initNoDependencyBeans(Set<Class<?>> classes) {
        return classes.stream()
                .map(Class::getConstructors)
                .flatMap(Arrays::stream)
                .filter(constructor -> constructor.getParameterCount() == 0)
                .map(this::initBean)
                .collect(Collectors.toList());
    }

    private Object initBean(Constructor<?> constructor, Object... dependencies) {
        try {
            return constructor.newInstance(dependencies);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to initialize given bean.");
        }
    }

    public <T> T getBean(final Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
