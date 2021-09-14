package domain.jr.faresystem.model.fare;

import domain.jr.externalsystems.util.currency.Yen;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Fare {
    Yen yen;

    public static Fare from(Yen yen) {
        Fare unvalidatedFare = new Fare(yen);

        return FareSpecification.validate(unvalidatedFare).getOrElseThrow(left -> new IllegalArgumentException(left + ": " + yen));
    }

    public static Fare from(int n) {
        return Fare.from(Yen.from(n));
    }

    public String show() {
        return "料金(" + yen.show() + ")";
    }

    public Fare _plus_(Fare that) {
        return Fare.from(this.yen._plus_(that.yen));
    }

    public Fare _minus_(Fare that) {
        return Fare.from(this.yen._minus_(that.yen));
    }
}
