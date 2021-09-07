package domain.jr.externalsystems.superexpress;

import java.util.Optional;

public interface SuperExpressRepository {
    Optional<SuperExpress> get(SuperExpressId id);
}
