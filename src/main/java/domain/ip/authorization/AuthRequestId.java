package domain.ip.authorization;

import domain.ip.util.LongId;
import lombok.ToString;

@ToString(callSuper = true)
public final class AuthRequestId extends LongId {
    AuthRequestId(long id) {
        super(id);
    }
}
