//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.service

import domain.jr.externalsystems.distance.StationDistanceRepositoryInMemory
import domain.jr.externalsystems.station.Station
import domain.jr.externalsystems.station.StationRepositoryInMemory
import domain.jr.externalsystems.superexpress.SuperExpress
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory
import domain.jr.externalsystems.util.time.Date
import domain.jr.faresystem.model.basicfare.BasicFareRepositoryInMemory
import domain.jr.faresystem.model.client.Client
import domain.jr.faresystem.model.situation.Party
import domain.jr.faresystem.model.situation.Situation
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepositoryInMemory
import spock.lang.Specification

import java.time.LocalDate

class FareComputationInMemorySpec extends Specification {

    private final FareComputation.FareComputationRepositories repositories = new FareComputation.FareComputationRepositories(
            new BasicFareRepositoryInMemory(),
            new SuperExpressSurchargeRepositoryInMemory(),
            new StationDistanceRepositoryInMemory()
    )

    static Station Tokyo = StationRepositoryInMemory.get_東京駅()
    static Station Shin_Osaka = StationRepositoryInMemory.get_新大阪駅()
    static Station Himeji = StationRepositoryInMemory.get_姫路駅()

    static SuperExpress Hikari = SuperExpressRepositoryInMemory.get_ひかり()
    static SuperExpress Nozomi = SuperExpressRepositoryInMemory.get_のぞみ()

    def "片道計算の動作確認"() {
        when:
        def party =
                Party.builder(
                        departure,
                        destination,
                        Date.from(LocalDate.parse(date)),
                        superExpress
                )
                        .addClientRepeatedly(Client.adult(), numOfAdults)
                        .addClientRepeatedly(Client.child(), numOfChildren)
                        .isFreeSeat(isFreeSeat)
                        .build()
        def situation =
                new Situation.SituationBuilder()
                        ._ゆき(party)
                        .build()
        def fareComputation = repositories.toFareComputationWith(situation)

        then:
        println fareComputation.account()
        println fareComputation.totalFare()
        assert true

        where:
        departure | destination | superExpress | numOfAdults | numOfChildren | isFreeSeat | date         || result
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 0             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 1             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 2             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 3             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 4             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 5             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 6             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 7             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 8             | true       | "2021-09-09" || 10_000
        Tokyo     | Shin_Osaka  | Hikari       | 1           | 9             | true       | "2021-09-09" || 10_000

        Himeji    | Tokyo       | Nozomi       | 1           | 1             | false      | "2021-01-09" || 10_000
        Himeji    | Tokyo       | Nozomi       | 2           | 1             | false      | "2021-01-09" || 10_000
        Himeji    | Tokyo       | Nozomi       | 3           | 1             | false      | "2021-01-09" || 10_000
        Himeji    | Tokyo       | Nozomi       | 4           | 1             | false      | "2021-01-09" || 10_000
    }

}
