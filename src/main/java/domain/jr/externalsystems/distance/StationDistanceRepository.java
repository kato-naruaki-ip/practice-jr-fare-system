package domain.jr.externalsystems.distance;

import domain.jr.externalsystems.station.Station;

import java.util.Optional;

public interface StationDistanceRepository {
    Optional<StationDistance> getBetween(Station departure, Station destination);
}
