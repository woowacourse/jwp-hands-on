package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans = new HashSet<>();

    public DIContainer(final Set<Class<?>> classes) {
        initContainer(classes);
    }

    /**
     * reflections로 아래 어노테이션이 붙은 애들을 빈으로 등록한다.
     * @Service : 클래스
     * @Repository : 클래스
     *
     */
    public static DIContainer createContainerForPackage(final String rootPackageName) {
        // @Service 가 붙은 클래스들의 인스턴스를 bean으로 등록하자.
        final Reflections reflections = new Reflections(rootPackageName);
        final Set<Class<?>> classes = new HashSet<>();
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        return new DIContainer(classes);
    }

    private void initContainer(final Set<Class<?>> classes) {
        initBean(classes);
        beans.forEach(this::setFields);
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

    // @Inject 붙은 필드의 의존성을 주입해준다.
    private void setFields(final Object bean) {
        final List<Field> fields = List.of(bean.getClass().getDeclaredFields());
        final List<Field> injectFields = fields.stream()
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
        injectFields.forEach(field -> setField(bean, field));
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
