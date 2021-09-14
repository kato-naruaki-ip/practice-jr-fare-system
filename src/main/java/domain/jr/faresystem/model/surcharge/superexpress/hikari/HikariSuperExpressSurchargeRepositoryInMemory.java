package domain.jr.faresystem.model.surcharge.superexpress.hikari;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.externalsystems.superexpress.SuperExpress;
import domain.jr.externalsystems.util.currency.Yen;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepositoryInMemory;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.Map;
import java.util.Optional;

public final class HikariSuperExpressSurchargeRepositoryInMemory implements HikariSuperExpressSurchargeRepository {
    private static final SuperExpress _ひかり = SuperExpressSurchargeRepositoryInMemory._ひかり;

    private static final Station _東京駅 = StationRepositoryInMemory.get_東京駅();
    private static final Station _新大阪駅 = StationRepositoryInMemory.get_新大阪駅();
    private static final Station _姫路駅 = StationRepositoryInMemory.get_姫路駅();

    private static final Map<Tuple2<Station, Station>, Yen> table = Map.of(
            Tuple.of(_東京駅, _新大阪駅), Yen.from(5_490),
            Tuple.of(_東京駅, _姫路駅), Yen.from(5_920)
    );

    @Override
    public Optional<SuperExpressSurcharge> getBetween(Station departure, Station destination) {
        Optional<Yen> got;

        got = Optional.ofNullable(table.get(Tuple.of(departure, destination)));
        if (got.isPresent()) {
            return Optional.of(new SuperExpressSurcharge(_ひかり, departure, destination, Fare.from(got.get())));
        }

        // departure と destination を入れ替えても同じ値段だと仮定する
        got = Optional.ofNullable(table.get(Tuple.of(destination, departure)));

        return got.map(yen -> new SuperExpressSurcharge(_ひかり, departure, destination, Fare.from(yen)));

    }
}
