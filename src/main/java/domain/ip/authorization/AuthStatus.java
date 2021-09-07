package domain.ip.authorization;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
enum AuthStatus {
    AUTHORIZED("承認済み"),
    UNDER_AUTHORIZATION("承認中");

    @Getter
    private final String asString;

    AuthStatus(@NonNull String asString) {
        this.asString = asString;
    }

    static AuthStatus ofDefault() {
        return UNDER_AUTHORIZATION;
    }
}
