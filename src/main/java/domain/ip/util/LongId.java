package domain.ip.util;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
abstract public class LongId {
    long id;

    public LongId(long id) {
        if (id <= 0) throw new IllegalArgumentException("id should be positive");

        this.id = id;
    }
}
