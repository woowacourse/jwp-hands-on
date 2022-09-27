package nextstep.study.di.stage4.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Java 표준의 의존성 주입 어노테이션, 스프링의 @Autowired와 동일. @Autowired 대체 가능!
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
