//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.model.surcharge.superexpress

import domain.jr.externalsystems.station.Station
import domain.jr.externalsystems.station.StationRepositoryInMemory
import domain.jr.externalsystems.superexpress.SuperExpress
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory
import domain.jr.faresystem.model.fare.Fare
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class SuperExpressSurchargeRepositoryInMemorySpec extends Specification {

    @Shared
    SuperExpressSurchargeRepository repository = new SuperExpressSurchargeRepositoryInMemory()

    static Station _東京駅 = StationRepositoryInMemory.get_東京駅()
    static Station _新大阪駅 = StationRepositoryInMemory.get_新大阪駅()
    static Station _姫路駅 = StationRepositoryInMemory.get_姫路駅()

    static SuperExpress _ひかり = SuperExpressRepositoryInMemory.get_ひかり()
    static SuperExpress _のぞみ = SuperExpressRepositoryInMemory.get_のぞみ()

    @Unroll
    def "#express.show()[ #departure.show()->#destination.show() ]の特急料金は[ #fareResult ]"() {
        given: "取得を試みる"
        Optional<SuperExpressSurcharge> got = repository.getBetween(express, departure, destination)

        expect:
        if (got.isEmpty()) {
            assert fare == null
        } else {
            assert got.get().getFare() == fare
            println got.get().show()
        }

        where:
        express | departure | destination || fareValue
        _ひかり    | _東京駅      | _東京駅        || null
        _ひかり    | _東京駅      | _新大阪駅       || 5490
        _ひかり    | _東京駅      | _姫路駅        || 5920
        _ひかり    | _新大阪駅     | _東京駅        || 5490
        _ひかり    | _新大阪駅     | _新大阪駅       || null
        _ひかり    | _新大阪駅     | _姫路駅        || null
        _ひかり    | _姫路駅      | _東京駅        || 5920
        _ひかり    | _姫路駅      | _新大阪駅       || null
        _ひかり    | _姫路駅      | _姫路駅        || null

        _のぞみ    | _東京駅      | _東京駅        || null
        _のぞみ    | _東京駅      | _新大阪駅       || 5490 + 320
        _のぞみ    | _東京駅      | _姫路駅        || 5920 + 530
        _のぞみ    | _新大阪駅     | _東京駅        || 5490 + 320
        _のぞみ    | _新大阪駅     | _新大阪駅       || null
        _のぞみ    | _新大阪駅     | _姫路駅        || null
        _のぞみ    | _姫路駅      | _東京駅        || 5920 + 530
        _のぞみ    | _姫路駅      | _新大阪駅       || null
        _のぞみ    | _姫路駅      | _姫路駅        || null

        fare = (fareValue == null) ? null : Fare.from(fareValue)
        fareResult = (fare == null) ? "未定義" : fare.getYen().show()
    }
}
