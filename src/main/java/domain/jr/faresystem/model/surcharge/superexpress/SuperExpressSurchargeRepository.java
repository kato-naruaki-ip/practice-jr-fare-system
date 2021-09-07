package domain.jr.faresystem.model.surcharge.superexpress;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.superexpress.SuperExpress;

import java.util.Optional;

public interface SuperExpressSurchargeRepository {
    Optional<SuperExpressSurcharge> getBetween(SuperExpress superExpress, Station departure, Station destination);
}
