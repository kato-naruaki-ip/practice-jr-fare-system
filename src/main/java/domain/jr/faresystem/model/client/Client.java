package domain.jr.faresystem.model.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Client {
    boolean isChild;

    public boolean isChild() {
        return isChild;
    }

    private static final Client ADULT = new Client(false);
    private static final Client CHILD = new Client(true);

    public static Client adult() {
        return ADULT;
    }

    public static Client child() {
        return CHILD;
    }
}
