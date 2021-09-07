package domain.ip.authorization;

import domain.ip.util.TimeStamp;
import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor(staticName = "ofDefault")
@Value
class Comment {

    String content = "<No Comment>";

    TimeStamp commentedAt = TimeStamp.ofDefault();
}
