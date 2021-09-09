package domain.jr.faresystem.model.basicfare;

import domain.jr.externalsystems.station.Station;
import domain.jr.faresystem.model.fare.Fare;
import lombok.NonNull;
import lombok.Value;

@Value
public class BasicFare {
    @NonNull
    Station departure;

    @NonNull
    Station destination;

    @NonNull
    Fare fare;
}
