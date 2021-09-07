package domain.jr.externalsystems.util.time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDate;

@Value
public class Date implements Comparable<Date> {
    @Getter(AccessLevel.PRIVATE)
    LocalDate date;

    @Getter(lazy = true)
    int year = date.getYear();

    @Getter(lazy = true)
    int month = date.getMonthValue();

    @Getter(lazy = true)
    int day = date.getDayOfMonth();

    private Date(LocalDate date) {
        this.date = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static Date from(LocalDate date) {
        return new Date(date);
    }

    @Override
    public int compareTo(Date that) {
        return this.date.compareTo(that.date);
    }

    public boolean isLessThanOrEqualTo(Date that) {
        return this.compareTo(that) <= 0;
    }
}
