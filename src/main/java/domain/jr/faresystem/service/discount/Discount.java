package domain.jr.faresystem.service.discount;

import domain.jr.faresystem.model.fare.Fare;

public interface Discount {
    Fare apply(Fare fare);
}
