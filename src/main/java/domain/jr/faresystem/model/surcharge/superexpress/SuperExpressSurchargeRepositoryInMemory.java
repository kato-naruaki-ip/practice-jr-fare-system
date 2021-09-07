package domain.jr.faresystem.model.surcharge.superexpress;

import domain.jr.externalsystems.station.Station;
import domain.jr.externalsystems.superexpress.SuperExpress;
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory;
import domain.jr.faresystem.model.surcharge.superexpress.hikari.HikariSuperExpressSurchargeRepositoryInMemory;
import domain.jr.faresystem.model.surcharge.superexpress.nozomi.NozomiSuperExpressSurchargeRepositoryInMemory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("NonAsciiCharacters")
@Repository
public final class SuperExpressSurchargeRepositoryInMemory implements SuperExpressSurchargeRepository {
    private static final SuperExpress _ひかり = SuperExpressRepositoryInMemory.get_ひかり();
    private static final SuperExpress _のぞみ = SuperExpressRepositoryInMemory.get_のぞみ();

    @Override
    public Optional<SuperExpressSurcharge> getBetween(SuperExpress superExpress, Station departure, Station destination) {
        if (superExpress.equals(_ひかり)) {
            return new HikariSuperExpressSurchargeRepositoryInMemory()
                    .getBetween(departure, destination);
        } else if (superExpress.equals(_のぞみ)) {
            return new NozomiSuperExpressSurchargeRepositoryInMemory(new HikariSuperExpressSurchargeRepositoryInMemory())
                    .getBetween(departure, destination);
        } else {
            return Optional.empty();
        }
    }
}
