package domain.jr.externalsystems.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StationRepositoryInMemoryTest {
    private final StationRepository repository = new StationRepositoryInMemory();

    @Test
    @DisplayName("get()メソッドの簡単な動作確認")
    void test_get() {
        Optional<Station> got;

        got = repository.get(new StationId(10L));
        assertTrue(got.isPresent());
        System.out.println(got);

        got = repository.get(new StationId(11L));
        assertTrue(got.isEmpty());
        System.out.println(got);

        got = repository.get(new StationId(20L));
        assertTrue(got.isPresent());
        System.out.println(got);

        got = repository.get(new StationId(21L));
        assertTrue(got.isEmpty());
        System.out.println(got);

        got = repository.get(new StationId(30L));
        assertTrue(got.isEmpty());
        System.out.println(got);

        got = repository.get(new StationId(35L));
        assertTrue(got.isPresent());
        System.out.println(got);
    }
}
