package domain.jr.faresystem.model.surcharge.superexpress;

import domain.jr.externalsystems.station.Station;
import domain.jr.faresystem.model.fare.Fare;
import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class SuperExpressSurcharge {
    @NonNull
    @Getter
    Station departure;

    @NonNull
    @Getter
    Station destination;

    @NonNull
    @Getter
    Fare fare;

    public String showFare() {
        return "特急料金(" + fare.getYen().show() + ")";
    }
}
