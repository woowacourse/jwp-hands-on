package nextstep.study.di.stage3.context;

import java.util.Set;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContext {

    private final Set<Object> beans;

    public DIContext(final Set<Class<?>> classes) {
        this.beans = Set.of();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> aClass) {
        return null;
    }
}
