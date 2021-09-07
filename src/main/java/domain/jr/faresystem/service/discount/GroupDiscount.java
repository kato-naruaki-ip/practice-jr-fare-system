package domain.jr.faresystem.service.discount;

import domain.jr.externalsystems.util.time.Date;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.fare.FareService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.Objects;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupDiscount {
    int groupSize;
    Date date;
    Fare oneClient;

    public static GroupDiscount when(int groupSize, Date date, Fare oneClient) {
        if (groupSize <= 0)
            throw new IllegalArgumentException("group size should be positive: " + groupSize);
        Objects.requireNonNull(date);
        Objects.requireNonNull(oneClient);

        return new GroupDiscount(groupSize, date, oneClient);
    }

    public Fare apply(Fare fare) {
        Fare result = fare;

        for (int i = 0; i < computeNumOfFree(); i++) {
            result = result._minus_(oneClient);
        }

        if (groupSize >= 8) {
            if (isNearNewYearsDay(date)) {
                result = FareService.discountByPercent(10, result);
            } else {
                result = FareService.discountByPercent(15, result);
            }
        }

        return result;
    }

    private int computeNumOfFree() {
        if (groupSize <= 30) {
            return 0;
        }

        //noinspection ConstantConditions
        if (30 < groupSize && groupSize <= 50) {
            return 1;
        }

        //noinspection ConstantConditions
        if (50 + 1 <= groupSize) {
            return 1 + (groupSize - 1) / 50;
        }

        throw new AssertionError();
    }

    @SuppressWarnings("OctalInteger")
    private static boolean isNearNewYearsDay(Date date) {
        int thisYearValue = date.getYear();

        Date s1 = Date.from(LocalDate.of(thisYearValue, 12, 21));
        Date e1 = Date.from(LocalDate.of(thisYearValue, 12, 31));

        Date s2 = Date.from(LocalDate.of(thisYearValue, 01, 01));
        Date e2 = Date.from(LocalDate.of(thisYearValue, 01, 10));

        return (s1.isLessThanOrEqualTo(date) && date.isLessThanOrEqualTo(e1)) ||
                (s2.isLessThanOrEqualTo(date) && date.isLessThanOrEqualTo(e2));
    }
}
