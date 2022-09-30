package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import nextstep.study.ConsumerWrapper;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    // 기본 생성자로 빈을 생성한다.
    private Set<Object> createBeans(final Set<Class<?>> classes) {
        Set<Object> beans = new HashSet<>();
        for (Class<?> aClass : classes) {
            Object instance = getInstance(aClass);
            beans.add(instance);
        }
        return beans;
    }

    private Object getInstance(Class<?> aClass) {
        try {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // 빈 내부에 선언된 필드를 각각 셋팅한다.
    private void setFields(final Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            setField(bean, field);
        }
    }

    // 각 필드에 빈을 대입(assign)한다.
    private void setField(final Object bean, final Field field) {
        final var type = field.getType();
        field.setAccessible(true);

        beans.stream()
                .filter(type::isInstance)
                .forEach(ConsumerWrapper.accept(matchBean -> field.set(bean, matchBean)));
    }
//        내가 구현한 코드
//        Optional<Object> sameTypeBean = findSameTypeBean(type);
//        if (sameTypeBean.isPresent()) {
//            try {
//                field.set(bean, sameTypeBean.get());
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private Optional<Object> findSameTypeBean(Class<?> type) {
//        return beans.stream()
//                .filter(type::isInstance)
//                .findFirst();
//    }

    // 빈 컨텍스트(DI)에서 관리하는 빈을 찾아서 반환한다.
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .map(bean -> (T) bean)
                .orElseThrow(() -> new IllegalArgumentException("Bean을 찾을 수 없습니다."));
    }
}
