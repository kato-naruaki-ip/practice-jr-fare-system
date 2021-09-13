package domain.jr.faresystem.service.discount;

import domain.jr.externalsystems.distance.StationDistance;
import domain.jr.externalsystems.util.length.KiloMeter;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.fare.FareRatio;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static domain.jr.faresystem.model.fare.FareService.multiplyRatio;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("NonAsciiCharacters")
public class RoundTripDiscount implements Discount {
    StationDistance distance;
    boolean is_往復;

    public static RoundTripDiscount when(StationDistance stationDistance, boolean is_往復) {
        return new RoundTripDiscount(stationDistance, is_往復);
    }

    @Override
    public String getName() {
        return "往復割引";
    }

    @Override
    public Fare apply(Fare fare) {
        return multiplyRatio(FareRatio.percent(computeDiscountPercentage()), fare);
    }

    @Override
    public String showDetail() {
        return String.format("割引率(%d%%)", computeDiscountPercentage());
    }

    private boolean isApplied() {
        return distance.getKiloMeter().isGreaterThanOrEqualTo(KiloMeter.from(601))
                && is_往復;
    }

    private int computeDiscountPercentage() {
        if (isApplied()) {
            return 90;
        } else {
            return 0;
        }
    }
}
