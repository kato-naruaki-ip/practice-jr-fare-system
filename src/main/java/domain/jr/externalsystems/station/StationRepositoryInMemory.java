package domain.jr.externalsystems.station;

import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
public final class StationRepositoryInMemory implements StationRepository {

    private static final StationId ID_TOKYO = new StationId(10L);
    @Getter
    private static final Station _東京駅 = new Station(ID_TOKYO, new StationName("東京"));

    private static final StationId ID_SHIN_OSAKA = new StationId(20L);
    @Getter
    private static final Station _新大阪駅 = new Station(ID_SHIN_OSAKA, new StationName("新大阪"));

    private static final StationId ID_HIMEJI = new StationId(35L);
    @Getter
    private static final Station _姫路駅 = new Station(ID_HIMEJI, new StationName("姫路"));

    private static final Map<StationId, Station> table = Map.of(
            ID_TOKYO, _東京駅,
            ID_SHIN_OSAKA, _新大阪駅,
            ID_HIMEJI, _姫路駅
    );

    @Override
    public Optional<Station> get(@NonNull StationId id) {
        return Optional.ofNullable(table.get(id));
    }
}
