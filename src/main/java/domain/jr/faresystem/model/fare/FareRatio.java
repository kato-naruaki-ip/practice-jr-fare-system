package domain.jr.faresystem.model.fare;

import domain.jr.externalsystems.util.currency.Yen;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.function.IntUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class FareRatio {
    @Getter(AccessLevel.PRIVATE)
    IntUnaryOperator convertor;

    public static FareRatio wari(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }

        return new FareRatio(n -> n * value / 10);
    }

    public static FareRatio percent(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }

        return new FareRatio(n -> n * value / 100);
    }

    public static FareRatio oneOver(int denominator) {
        if (denominator <= 0) {
            throw new IllegalArgumentException();
        }

        return new FareRatio(n -> n / denominator);
    }

    Yen multiply(Yen yen) {
        return Yen.from(convertor.applyAsInt(yen.getValue()));
    }
}
