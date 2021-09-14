package domain.jr.faresystem.model.basicfare;

import domain.jr.externalsystems.station.Station;

import java.util.Optional;

public interface BasicFareRepository {
    Optional<BasicFare> getBetween(Station departure, Station destination);
}
