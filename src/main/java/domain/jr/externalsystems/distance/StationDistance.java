package domain.jr.externalsystems.distance;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.util.length.KiloMeter;
import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class StationDistance {
    @NonNull
    @Getter
    Station departure;

    @NonNull
    @Getter
    Station destination;

    @NonNull
    @Getter
    KiloMeter kiloMeter;
}
