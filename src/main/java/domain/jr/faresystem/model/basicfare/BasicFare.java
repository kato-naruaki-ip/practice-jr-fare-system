package domain.jr.faresystem.model.basicfare;

import domain.jr.externalsystems.station.Station;
import domain.jr.faresystem.model.fare.Fare;
import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class BasicFare {
    @NonNull
    @Getter
    Station departure;

    @NonNull
    @Getter
    Station destination;

    @NonNull
    @Getter
    Fare fare;
}
