package domain.jr.faresystem.service.discount;

import domain.jr.faresystem.model.client.Client;
import domain.jr.faresystem.model.fare.Fare;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static domain.jr.faresystem.model.fare.FareService.halfOf;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChildDiscount {
    boolean isChild;

    public static ChildDiscount when(Client client) {
        return new ChildDiscount(client.isChild());
    }

    public static Fare computeTotalFareForChild(Fare basicFare, Fare superExpressSurchargeFare) {
        return halfOf(basicFare)._plus_(halfOf(superExpressSurchargeFare));
    }

    public Fare apply(Fare fare) {
        if (isChild) {
            return halfOf(fare);
        } else {
            return fare;
        }
    }
}
