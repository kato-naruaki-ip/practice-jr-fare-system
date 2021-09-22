package domain.jr.faresystem.model.surcharge.superexpress;

import domain.jr.externalsystems.station.Station;

import java.util.Optional;

public interface SingleSuperExpressSurchargeRepository {
    Optional<SuperExpressSurcharge> getBetween(Station departure, Station destination);
}
