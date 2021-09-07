package domain.jr.externalsystems.superexpress;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class SuperExpress {
    @NonNull
    @Getter
    SuperExpressId id;

    @NonNull
    @Getter
    SuperExpressName name;

    public String show() {
        return "特急(" + name.getName() + ")";
    }
}
