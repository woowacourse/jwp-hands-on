package nextstep.study.di.stage3.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();

    // TODO: 같은 클래스로 여러 빈 등록 불가하게 수정해야한다.
    public DIContainer(final Set<Class<?>> classes) {
        // 클래스를 인스턴스화 해서 Set에 저장
        initBean(classes);
        // 빈의 필드에 빈으로 등록할 수 있는 필드 등록
        beans.forEach(this::setFields);
    }

    private void setFields(final Object bean) {
        // 필드가 빈으로 등록되어있으면 주입한다.
        final List<Field> fields = List.of(bean.getClass().getDeclaredFields());
        fields.forEach(field -> setField(bean, field));
    }

    private void setField(final Object obj, final Field field) {
        field.setAccessible(true);
        beans.forEach(bean -> setFieldValue(obj, field, bean));
    }

    private void setFieldValue(final Object obj, final Field field, final Object bean) {
        final Class<?> fieldType = field.getType();
        if (fieldType.isInstance(bean)) {
            try {
                field.set(obj, bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initBean(final Set<Class<?>> classes) {
        try {
            for (Class<?> aClass : classes) {
                addBeans(aClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBeans(final Class<?> aClass)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Constructor<?> constructor = aClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        final Object instance = constructor.newInstance();
        beans.add(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return (T) beans.stream()
                .filter(aClass::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("등록된 빈이 아닙니다."));
    }
}
