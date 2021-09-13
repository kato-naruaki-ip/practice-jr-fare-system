package domain.jr.faresystem.service.discount;

import domain.jr.faresystem.model.fare.Fare;

public interface Discount {
    String getName();

    Fare apply(Fare fare);

    String showDetail();
}
