package domain.jr.externalsystems.util.length;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KiloMeter implements Comparable<KiloMeter> {
    int value;

    public static KiloMeter from(int value) {
        return new KiloMeter(value);
    }

    @Override
    public int compareTo(KiloMeter that) {
        return this.value - that.value;
    }

    public boolean isGreaterThanOrEqualTo(KiloMeter that) {
        return this.compareTo(that) >= 0;
    }
}
