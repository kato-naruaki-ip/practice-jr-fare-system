package domain.jr.faresystem.model.surcharge.superexpress.nozomi;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.externalsystems.superexpress.SuperExpress;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepositoryInMemory;
import domain.jr.faresystem.model.surcharge.superexpress.hikari.HikariSuperExpressSurchargeRepository;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
public final class NozomiSuperExpressSurchargeRepositoryInMemory implements NozomiSuperExpressSurchargeRepository {
    private final HikariSuperExpressSurchargeRepository hikariSuperExpressSurchargeRepository;

    private static final SuperExpress _のぞみ = SuperExpressSurchargeRepositoryInMemory._のぞみ;

    private static final Station _東京駅 = StationRepositoryInMemory.get_東京駅();
    private static final Station _新大阪駅 = StationRepositoryInMemory.get_新大阪駅();
    private static final Station _姫路駅 = StationRepositoryInMemory.get_姫路駅();

    public NozomiSuperExpressSurchargeRepositoryInMemory(HikariSuperExpressSurchargeRepository hikariSuperExpressSurchargeRepository) {
        this.hikariSuperExpressSurchargeRepository = hikariSuperExpressSurchargeRepository;
    }

    @Override
    public Optional<SuperExpressSurcharge> getBetween(Station departure, Station destination) {
        if (hasSameElements(Tuple.of(departure, destination), Tuple.of(_東京駅, _新大阪駅))) {
            return hikariSuperExpressSurchargeRepository.getBetween(departure, destination)
                    .map(surcharge ->
                            new SuperExpressSurcharge(_のぞみ, departure, destination, surcharge.getFare()._plus_(Fare.from(320)))
                    );
        } else if (hasSameElements(Tuple.of(departure, destination), Tuple.of(_東京駅, _姫路駅))) {
            return hikariSuperExpressSurchargeRepository.getBetween(departure, destination)
                    .map(surcharge ->
                            new SuperExpressSurcharge(_のぞみ, departure, destination, surcharge.getFare()._plus_(Fare.from(530)))
                    );
        } else {
            return Optional.empty();
        }
    }

    private static <T> boolean hasSameElements(Tuple2<T, T> x, Tuple2<T, T> y) {
        return (x._1.equals(y._1) && x._2.equals(y._2)) ||
                (x._1.equals(y._2) && x._2.equals(y._1));
    }
}
