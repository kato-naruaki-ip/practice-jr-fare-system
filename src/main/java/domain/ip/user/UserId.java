package domain.ip.user;

import domain.ip.util.LongId;
import lombok.ToString;

@ToString(callSuper = true)
public final class UserId extends LongId {
    UserId(long id) {
        super(id);
    }
}
