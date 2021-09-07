package domain.ip.authorization;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
enum RequestStatus {
    ACTIVE("アクティブ"),
    INACTIVE("非アクティブ");

    @Getter
    private final String asString;

    RequestStatus(@NonNull String asString) {
        this.asString = asString;
    }
}
