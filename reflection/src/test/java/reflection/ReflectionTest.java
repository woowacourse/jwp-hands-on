package reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void givenObject_whenGetsClassName_thenCorrect() {
        final Class<Question> clazz = Question.class;

        assertThat(clazz.getSimpleName()).isEqualTo("Question");
        assertThat(clazz.getName()).isEqualTo("reflection.Question");
        assertThat(clazz.getCanonicalName()).isEqualTo("reflection.Question");
    }

    @Test
    void givenClassName_whenCreatesObject_thenCorrect() throws Exception {
        final Class<?> clazz = Class.forName("reflection.Question");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        final var object = constructor.newInstance("gugu", "title", "content");

        assertThat(object.getClass().getSimpleName()).isEqualTo("Question");
        assertThat(object).isInstanceOf(Question.class);
    }

    /**
     * getDeclaredFields(): public, protected, default (package), private 필드 목록 반환
     * - 부모로부터 상속받은 필드들은 제외
     * getFields(): public 필드 목록만 반환 => Useless
     * - 부모로부터 상속받은 필드들 포함
     */
    @Test
    void givenObject_whenGetsFieldNamesAtRuntime_thenCorrect() {
        final Object student = new Student();
        final Field[] publicFields = student.getClass().getFields();
        final Field[] fields = student.getClass().getDeclaredFields();
        for (final var field : fields) {
            log.info("{} 필드: {} 타입", field.getName(), field.getType());
        }
        final List<String> actualFieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        assertThat(publicFields).isEmpty();
        assertThat(actualFieldNames).contains("name", "age");
    }

    /**
     * getDeclaredMethods(): public, protected, default (package), private 메서드 목록 반환
     * - 부모로부터 상속받은 메서드들 제외
     * getMethods(): public 메서드 목록만 반환
     * - 주의: 상속받은 메서드들 포함
     */
    @Test
    void givenClass_whenGetsMethods_thenCorrect() {
        final Class<?> animalClass = Student.class;
        final Method[] methods = animalClass.getDeclaredMethods();
        final List<String> actualMethods = Arrays.stream(methods)
                .map(Method::getName)
                .collect(Collectors.toList());

        assertThat(actualMethods)
                .hasSize(3)
                .contains("getAge", "toString", "getName");
    }

    /**
     * getConstructors(): public 생성자 목록만 반환
     * getDeclaredConstructors(): public, protected, default (package), private 생성자 목록 반환
     */
    @Test
    void givenClass_whenGetsAllConstructors_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Constructor<?>[] publicConstructors = questionClass.getConstructors();
        final Constructor<?>[] constructors = questionClass.getDeclaredConstructors();

        assertThat(publicConstructors).hasSize(2);
        assertThat(constructors).hasSize(2);
    }

    @Test
    void givenClass_whenInstantiatesObjectsAtRuntime_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;

        final Constructor<?> firstConstructor = questionClass.getConstructor(String.class, String.class, String.class);
        final Constructor<?> secondConstructor = questionClass.getConstructor(
                Long.TYPE, String.class, String.class, String.class, Date.class, Integer.TYPE);

        final var firstQuestion = (Question) firstConstructor.newInstance("gugu", "제목1", "내용1");
        final Question secondQuestion = (Question) secondConstructor.newInstance(
                0, "gugu", "제목2", "내용2", new Date(), 0);

        assertThat(firstQuestion.getWriter()).isEqualTo("gugu");
        assertThat(firstQuestion.getTitle()).isEqualTo("제목1");
        assertThat(firstQuestion.getContents()).isEqualTo("내용1");
        assertThat(secondQuestion.getWriter()).isEqualTo("gugu");
        assertThat(secondQuestion.getTitle()).isEqualTo("제목2");
        assertThat(secondQuestion.getContents()).isEqualTo("내용2");
    }

    @Test
    void givenClass_whenGetsPublicFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getFields();

        assertThat(fields).hasSize(0);
    }

    @Test
    void givenClass_whenGetsDeclaredFields_thenCorrect() {
        final Class<?> questionClass = Question.class;
        final Field[] fields = questionClass.getDeclaredFields();

        assertThat(fields).hasSize(6);
        assertThat(fields[0].getName()).isEqualTo("questionId");
    }

    /**
     * getField(~) : public 필드들 중에서 이름으로 찾기
     * getDeclaredField(~) : 클래스에 정의된 필드 중에서 이름으로 찾기
     */
    @Test
    void givenClass_whenGetsFieldsByName_thenCorrect() throws Exception {
        final Class<?> questionClass = Question.class;
        final Field field = questionClass.getDeclaredField("questionId");

        assertThat(field.getName()).isEqualTo("questionId");
    }

    @Test
    void givenClassField_whenGetsType_thenCorrect() throws Exception {
        final Field field = Question.class.getDeclaredField("questionId");
        final Class<?> fieldClass = field.getType();

        assertThat(fieldClass.getSimpleName()).isEqualTo("long");
    }

    @Test
    void givenClassField_whenSetsAndGetsValue_thenCorrect() throws Exception {
        final Class<?> studentClass = Student.class;
        final var student = (Student) studentClass.getConstructor().newInstance();
        final Field field = studentClass.getDeclaredField("age");

        field.setAccessible(true);
        assertThat(field.getInt(student)).isZero();
        assertThat(student.getAge()).isZero();

        field.set(student, 99);
        assertThat(field.getInt(student)).isEqualTo(99);
        assertThat(student.getAge()).isEqualTo(99);

        field.setAccessible(false);
    }
}
