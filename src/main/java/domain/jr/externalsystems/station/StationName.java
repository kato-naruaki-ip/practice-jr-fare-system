package domain.jr.externalsystems.station;

import lombok.NonNull;
import lombok.Value;

@Value
class StationName {
    @NonNull
    String name;
}
