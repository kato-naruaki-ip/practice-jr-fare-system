package domain.jr.faresystem.model.surcharge.superexpress;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.externalsystems.superexpress.SuperExpress;
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("NonAsciiCharacters")
class SuperExpressSurchargeRepositoryInMemoryTest {
    private final SuperExpressSurchargeRepository repository = new SuperExpressSurchargeRepositoryInMemory();

    private static final Station _東京駅 = StationRepositoryInMemory.get_東京駅();
    private static final Station _新大阪駅 = StationRepositoryInMemory.get_新大阪駅();
    private static final Station _姫路駅 = StationRepositoryInMemory.get_姫路駅();

    private static final SuperExpress _ひかり = SuperExpressRepositoryInMemory.get_ひかり();
    private static final SuperExpress _のぞみ = SuperExpressRepositoryInMemory.get_のぞみ();

    @Test
    @DisplayName("getBetween()メソッドの簡単な動作確認")
    void test_getBetween() {
        testWith(_ひかり, _東京駅, _東京駅, false);
        testWith(_ひかり, _東京駅, _東京駅, false);
        testWith(_ひかり, _東京駅, _新大阪駅, true);
        testWith(_ひかり, _東京駅, _姫路駅, true);
        testWith(_ひかり, _新大阪駅, _東京駅, true);
        testWith(_ひかり, _新大阪駅, _新大阪駅, false);
        testWith(_ひかり, _新大阪駅, _姫路駅, false);
        testWith(_ひかり, _姫路駅, _東京駅, true);
        testWith(_ひかり, _姫路駅, _新大阪駅, false);
        testWith(_ひかり, _姫路駅, _姫路駅, false);

        testWith(_のぞみ, _東京駅, _新大阪駅, true);
        testWith(_のぞみ, _東京駅, _姫路駅, true);
        testWith(_のぞみ, _新大阪駅, _東京駅, true);
        testWith(_のぞみ, _新大阪駅, _新大阪駅, false);
        testWith(_のぞみ, _新大阪駅, _姫路駅, false);
        testWith(_のぞみ, _姫路駅, _東京駅, true);
        testWith(_のぞみ, _姫路駅, _新大阪駅, false);
        testWith(_のぞみ, _姫路駅, _姫路駅, false);
    }

    private void testWith(SuperExpress superExpress, Station departure, Station destination, boolean isPresent) {
        Optional<SuperExpressSurcharge> got;
        got = repository.getBetween(superExpress, departure, destination);
        assertEquals(isPresent, got.isPresent());
        System.out.println(
                got.map(SuperExpressSurcharge::show).orElse("<Undefined>")
        );
    }
}
