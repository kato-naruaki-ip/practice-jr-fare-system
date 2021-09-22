package domain.jr.externalsystems.superexpress;

import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
public final class SuperExpressRepositoryInMemory implements SuperExpressRepository {

    private static final SuperExpressId ID_ひかり = new SuperExpressId(830);
    @Getter
    private static final SuperExpress _ひかり = new SuperExpress(ID_ひかり, new SuperExpressName("ひかり"));

    private static final SuperExpressId ID_のぞみ = new SuperExpressId(1037);
    @Getter
    private static final SuperExpress _のぞみ = new SuperExpress(ID_のぞみ, new SuperExpressName("のぞみ"));

    private static final Map<SuperExpressId, SuperExpress> table = Map.of(
            ID_ひかり, _ひかり,
            ID_のぞみ, _のぞみ
    );

    @Override
    public Optional<SuperExpress> get(@NonNull SuperExpressId id) {
        return Optional.ofNullable(table.get(id));
    }
}
