package domain.ip.user;

import lombok.NonNull;
import lombok.Value;

@Value
public class UserName {
    @NonNull
    String firstName;

    @NonNull
    String lastName;
}
