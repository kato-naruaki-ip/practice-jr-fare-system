package domain.jr.faresystem.service.discount;

import domain.jr.faresystem.model.fare.Fare;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeSeatDiscount implements Discount {
    boolean isFreeSeat;

    private static final Fare DISCOUNT_FARE = Fare.from(530);

    public static FreeSeatDiscount when(boolean isFreeSeat) {
        return new FreeSeatDiscount(isFreeSeat);
    }

    @Override
    public String getName() {
        return "自由席特急料金";
    }

    @Override
    public Fare apply(Fare fare) {
        if (isFreeSeat) {
            return fare._minus_(DISCOUNT_FARE);
        } else {
            return fare;
        }
    }

    @Override
    public String showDetail() {
        return isFreeSeat ? String.format("減額(%s)", DISCOUNT_FARE.getYen().show()) : "適用なし";
    }
}
