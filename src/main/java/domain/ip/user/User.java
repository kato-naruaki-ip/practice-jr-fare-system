package domain.ip.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"id"})
public final class User {
    private final UserId id;

    @Getter
    private final UserName name;

    User(long id, @NonNull UserName name) {
        this.id = new UserId(id);
        this.name = name;
    }

    public static User ofDefault() {
        return new User(1L, new UserName("Taro", "Yamada"));
    }
}
