package domain.jr.faresystem.model.fare;

import io.vavr.control.Either;
import lombok.NonNull;

final class FareSpecification {
    static final int FARE_UNIT_VALUE = 10;

    public static Either<String, Fare> validate(@NonNull Fare fare) {
        if (fare.getYen() == null) {
            return Either.left("Invalid value: null");
        }

        if (fare.getYen().getValue() < 0) {
            return Either.left("Invalid value: negative");
        }

        if (fare.getYen().getValue() % FARE_UNIT_VALUE != 0) {
            return Either.left("Invalid value: indivisible by unit");
        }

        return Either.right(fare);
    }
}
