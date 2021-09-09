//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.model.basicfare

import domain.jr.externalsystems.station.Station
import domain.jr.faresystem.model.fare.Fare
import org.mockito.Mockito
import spock.lang.Specification

class BasicFareSpec extends Specification {
    static Station mockStation1 = Mockito.mock(Station.class)
    static Station mockStation2 = Mockito.mock(Station.class)
    static Fare mockFare = Mockito.mock(Fare.class)

    def "null をコンストラクターに与えると NullPointerException"() {
        when:
        new BasicFare(station1, station2, fare)

        then:
        thrown NullPointerException

        where:
        station1     | station2     | fare
        null         | mockStation2 | mockFare
        mockStation1 | null         | mockFare
        mockStation1 | mockStation2 | null
    }

}
