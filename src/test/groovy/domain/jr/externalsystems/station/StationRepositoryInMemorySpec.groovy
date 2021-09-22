//file:noinspection NonAsciiCharacters
package domain.jr.externalsystems.station

import spock.lang.Shared
import spock.lang.Specification

class StationRepositoryInMemorySpec extends Specification {
    @Shared
    StationRepository repository = new StationRepositoryInMemory()

    def "id = #id の駅は #name"() {
        given:
        Optional<Station> stationOpt = repository.get(new StationId(id))

        expect:
        assert stationOpt.isPresent()
        def station = stationOpt.get()
        assert station.getName().getName() == name

        where:
        id || name
        10 || "東京"
        20 || "新大阪"
        35 || "姫路"
    }

    def "id = #id の駅は存在しない"() {
        given:
        Optional<Station> stationOpt = repository.get(new StationId(id))

        expect:
        assert stationOpt.isEmpty()

        where:
        id | _
        0  | _
        1  | _
        9  | _
        11 | _
        19 | _
        21 | _
        34 | _
        36 | _
    }
}
