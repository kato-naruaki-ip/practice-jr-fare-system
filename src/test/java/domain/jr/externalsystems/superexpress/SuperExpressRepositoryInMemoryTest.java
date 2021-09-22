package domain.jr.externalsystems.superexpress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperExpressRepositoryInMemoryTest {
    private final SuperExpressRepository repository = new SuperExpressRepositoryInMemory();

    @Test
    @DisplayName("get()メソッドの簡単な動作確認")
    void test_get() {
        Optional<SuperExpress> got;

        got = repository.get(new SuperExpressId(830));
        assertTrue(got.isPresent());
        System.out.println(got);

        got = repository.get(new SuperExpressId(1));
        assertTrue(got.isEmpty());
        System.out.println(got);

        got = repository.get(new SuperExpressId(1037));
        assertTrue(got.isPresent());
        System.out.println(got);
    }
}
