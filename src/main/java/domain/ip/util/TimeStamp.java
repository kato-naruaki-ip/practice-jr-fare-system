package domain.ip.util;

import lombok.Value;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Value
public class TimeStamp {
    LocalDateTime time;

    public static TimeStamp now() {
        return new TimeStamp(LocalDateTime.now());
    }

    public static TimeStamp ofDefault() {
        return new TimeStamp(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC));
    }
}
