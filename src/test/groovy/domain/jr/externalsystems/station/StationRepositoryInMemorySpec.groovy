//file:noinspection NonAsciiCharacters
package domain.jr.externalsystems.station

import spock.lang.Shared
import spock.lang.Specification

class StationRepositoryInMemorySpec extends Specification {
    @Shared
    StationRepository repository = new StationRepositoryInMemory()

    def "id = #id の駅は #stationResult"() {
        given:
        Optional<Station> stationOpt = repository.get(new StationId(id))

        expect:
        if (stationOpt.isEmpty()) {
            assert name == null
        } else {
            def station = stationOpt.get()
            assert station.getName().getName() == name
            println station
        }

        where:
        id || name
        -2 || null
        -1 || null
        0 || null
        1 || null
        2 || null
        3 || null
        4 || null
        5 || null
        6 || null
        7 || null
        8 || null
        9 || null
        10 || "東京"
        11 || null
        12 || null
        13 || null
        14 || null
        15 || null
        16 || null
        17 || null
        18 || null
        19 || null
        20 || "新大阪"
        21 || null
        22 || null
        23 || null
        24 || null
        25 || null
        26 || null
        27 || null
        28 || null
        29 || null
        30 || null
        31 || null
        32 || null
        33 || null
        34 || null
        35 || "姫路"
        36 || null

        stationResult = (name == null) ? "未定義" : "< $name >"
    }
}
