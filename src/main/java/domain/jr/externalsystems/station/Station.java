package domain.jr.externalsystems.station;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Station {
    @NonNull
    @Getter
    StationId id;

    @NonNull
    @Getter
    StationName name;

    public String show() {
        return name.getName() + "駅";
    }
}
