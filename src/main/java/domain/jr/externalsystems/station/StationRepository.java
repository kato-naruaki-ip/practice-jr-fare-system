package domain.jr.externalsystems.station;

import java.util.Optional;

public interface StationRepository {
    Optional<Station> get(StationId id);
}
