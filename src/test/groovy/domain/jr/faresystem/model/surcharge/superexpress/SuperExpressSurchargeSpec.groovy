//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.model.surcharge.superexpress

import domain.jr.externalsystems.station.Station
import domain.jr.externalsystems.superexpress.SuperExpress
import domain.jr.faresystem.model.fare.Fare
import org.mockito.Mockito
import spock.lang.Specification

class SuperExpressSurchargeSpec extends Specification {
    static SuperExpress mockSuperExpress = Mockito.mock(SuperExpress.class)
    static Station mockStation1 = Mockito.mock(Station.class)
    static Station mockStation2 = Mockito.mock(Station.class)
    static Fare mockFare = Mockito.mock(Fare.class)

    def "null をコンストラクターに与えると NullPointerException"() {
        when:
        new SuperExpressSurcharge(superExpress, station1, station2, fare)

        then:
        thrown NullPointerException

        where:
        superExpress     | station1     | station2     | fare
        null             | mockStation1 | mockStation2 | mockFare
        mockSuperExpress | null         | mockStation2 | mockFare
        mockSuperExpress | mockStation1 | null         | mockFare
        mockSuperExpress | mockStation1 | mockStation2 | null
    }
}
