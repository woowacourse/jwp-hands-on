package nextstep.study.di.stage4.annotations;

import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    public DIContainer(final Set<Class<?>> classes) {
        this.beans = Set.of();
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return null;
    }
}
