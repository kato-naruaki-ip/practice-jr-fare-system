package domain.jr.faresystem.model.fare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FareTest {
    @Test
    void constructing_from_null_yen_throws_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Fare.from(null));
    }

    @Test
    void constructing_from_negative_yen_throws_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Fare.from(-20));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(-10));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(-1));
    }

    @Test
    void constructing_from_indivisible_yen_throws_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Fare.from(1));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(2));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(3));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(11));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(12));
        assertThrows(IllegalArgumentException.class, () -> Fare.from(13));
    }

    @Test
    void zero_yen_is_valid() {
        Fare zero = Fare.from(0);
        assertEquals(0, zero.getYen().getValue());
    }
}
