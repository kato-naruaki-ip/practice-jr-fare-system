package domain.jr.externalsystems.util.currency;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Yen {
    int value;

    public static Yen from(int n) {
        return new Yen(n);
    }

    public String show() {
        return String.format("%,3d円", value);
    }

    public Yen _plus_(Yen that) {
        return Yen.from(this.value + that.value);
    }

    public Yen _minus_(Yen that) {
        return Yen.from(this.value - that.value);
    }
}
