package transaction.stage0;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * ACID 기본 원칙이 무엇인지 학습해보자.
 * 그리고 answer 변수에 트랜잭션의 특성을 영어 단어로 작성해서 테스트를 통과시켜보자.
 * 예) final var answer = "database";
 *
 * ACID
 * https://en.wikipedia.org/wiki/ACID
 *
 * Database Basics: ACID Transactions
 * https://towardsdatascience.com/database-basics-acid-transactions-bf4d38bd8e26
 */
class Stage0Test {

    // 동시에 실행되는 트랜잭션이 서로에게 영향을 미치지 못하도록 보장하는 트랜잭션의 특성은?
    @Test
    void quiz1() {
        final var answer = "isolation";
        assertThat(Sha256.encrypt(answer.toLowerCase()))
                .isEqualTo("3624d3181d5c4f8abf2f25fa708f5efa04236b79d0deafe9f292b590b2ca0f7e");
    }

    // 트랜잭션을 성공적으로 실행하면 그 결과가 항상 기록되어야 하는 트랜잭션의 특성은?
    @Test
    void quiz2() {
        final var answer = "durability";
        assertThat(Sha256.encrypt(answer.toLowerCase()))
                .isEqualTo("2731dedfe254c1d83b35853d325b5218e66fead073a6abcfbe9f568c84d43473");
    }

    // 트랜잭션이 성공적으로 완료하면 언제나 동일한 데이터베이스 상태로 유지하는 것을 의미하는 트랜잭션의 특성은?
    @Test
    void quiz3() {
        final var answer = "consistency";
        assertThat(Sha256.encrypt(answer.toLowerCase()))
                .isEqualTo("0d00a53b5550a5f409891471880956f3a4b060c31b7a73c15149e051e5c24ce5");
    }

    // 트랜잭션이 완전히 성공하거나 완전히 실패하는 단일 단위로 처리되도록 보장하는 트랜잭션의 특성은?
    @Test
    void quiz4() {
        final var answer = "atomicity";
        assertThat(Sha256.encrypt(answer.toLowerCase()))
                .isEqualTo("3931c975909268ae950c3d126f70efd9158ed6168241ed66713a5ff47d7a2d4d");
    }
}
