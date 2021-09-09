package domain.jr.faresystem.model.surcharge.superexpress;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.superexpress.SuperExpress;
import domain.jr.faresystem.model.fare.Fare;
import lombok.NonNull;
import lombok.Value;

@Value
public class SuperExpressSurcharge {
    @NonNull
    SuperExpress superExpress;

    @NonNull
    Station departure;

    @NonNull
    Station destination;

    @NonNull
    Fare fare;

    public String showFare() {
        return "特急料金(" + fare.getYen().show() + ")";
    }
}
