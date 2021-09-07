package domain.jr.faresystem.service.discount;

import domain.jr.faresystem.model.fare.Fare;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeSeatDiscount {
    boolean isFreeSeat;

    public static FreeSeatDiscount when(boolean isFreeSeat) {
        return new FreeSeatDiscount(isFreeSeat);
    }

    public Fare apply(Fare fare) {
        if (isFreeSeat) {
            return fare._minus_(Fare.from(530));
        } else {
            return fare;
        }
    }
}
