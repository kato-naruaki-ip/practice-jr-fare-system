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

    public String show() {
        return String.format("特急料金(%s):[%s]%s->%s", fare.getYen().show(), superExpress.show(), departure.show(), destination.show());
    }
}
