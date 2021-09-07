package domain.ip.authorization;

import domain.ip.user.User;
import domain.ip.util.TimeStamp;
import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(staticName = "ofDefault")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
final class Assignee {

    AssigneeId id = new AssigneeId(1L);

    @Getter
    User user = User.ofDefault();

    @Getter
    AuthStatus status = AuthStatus.ofDefault();

    @Getter
    TimeStamp checkedAt = TimeStamp.ofDefault();

    @Getter
    Comment comment = Comment.ofDefault();
}
