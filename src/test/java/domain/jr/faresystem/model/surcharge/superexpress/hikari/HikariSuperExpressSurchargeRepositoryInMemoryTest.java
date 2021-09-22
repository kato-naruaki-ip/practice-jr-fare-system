package domain.jr.faresystem.model.surcharge.superexpress.hikari;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.faresystem.model.surcharge.superexpress.SingleSuperExpressSurchargeRepository;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HikariSuperExpressSurchargeRepositoryInMemoryTest {
    private final SingleSuperExpressSurchargeRepository repository = new HikariSuperExpressSurchargeRepositoryInMemory();

    private static final Station _東京駅 = StationRepositoryInMemory.get_東京駅();
    private static final Station _新大阪駅 = StationRepositoryInMemory.get_新大阪駅();
    private static final Station _姫路駅 = StationRepositoryInMemory.get_姫路駅();

    @Test
    @DisplayName("getBetween()メソッドの簡単な動作確認")
    void test_getBetween() {
        Optional<SuperExpressSurcharge> got;

        got = repository.getBetween(_東京駅, _新大阪駅);
        assertTrue(got.isPresent());
        System.out.println(got);

        got = repository.getBetween(_東京駅, _姫路駅);
        assertTrue(got.isPresent());
        System.out.println(got);

        got = repository.getBetween(_東京駅, _東京駅);
        assertTrue(got.isEmpty());
        System.out.println(got);

        got = repository.getBetween(_新大阪駅, _東京駅);
        assertTrue(got.isPresent());
        System.out.println(got);

        got = repository.getBetween(_姫路駅, _東京駅);
        assertTrue(got.isPresent());
        System.out.println(got);
    }

}
