package domain.jr.faresystem.model.fare;

import domain.jr.externalsystems.util.currency.Yen;

public final class FareService {
    private static int roundDown(float x, int radix) {
        switch (radix) {
            case 1:
                return (int) x;
            case 10:
                return ((int) x / 10) * 10;
            default:
                throw new IllegalArgumentException("radix: " + radix);
        }
    }

    private static int roundDown(float x) {
        return roundDown(x, FareSpecification.FARE_UNIT_VALUE);
    }

    private static Yen roundDownYen(Yen yen) {
        return Yen.from(roundDown(yen.getValue()));
    }

    public static Fare multiplyRatio(FareRatio ratio, Fare fare) {
        return Fare.from(
                roundDownYen(ratio.multiply(fare.getYen()))
        );
    }

    public static Fare halfOf(Fare fare) {
        return multiplyRatio(FareRatio.oneOver(2), fare);
    }

    public static Fare discountByPercent(int percent, Fare fare) {
        return multiplyRatio(FareRatio.percent(100 - percent), fare);
    }

}
