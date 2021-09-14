package domain.jr.externalsystems.distance;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.externalsystems.util.length.KiloMeter;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
@Repository
public final class StationDistanceRepositoryInMemory implements StationDistanceRepository {
    private static final Station _東京駅 = StationRepositoryInMemory.get_東京駅();
    private static final Station _新大阪駅 = StationRepositoryInMemory.get_新大阪駅();
    private static final Station _姫路駅 = StationRepositoryInMemory.get_姫路駅();

    private static final Map<Tuple2<Station, Station>, KiloMeter> table = Map.of(
            Tuple.of(_東京駅, _新大阪駅), KiloMeter.from(553),
            Tuple.of(_東京駅, _姫路駅), KiloMeter.from(644)
    );

    @Override
    public Optional<StationDistance> getBetween(Station departure, Station destination) {
        Optional<KiloMeter> got;

        got = Optional.ofNullable(table.get(Tuple.of(departure, destination)));
        if (got.isPresent()) {
            return Optional.of(new StationDistance(departure, destination, got.get()));
        }

        // departure と destination を入れ替えても同じだと仮定する
        got = Optional.ofNullable(table.get(Tuple.of(destination, departure)));

        return got.map(kiloMeter -> new StationDistance(departure, destination, kiloMeter));
    }
}
