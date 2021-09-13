//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.service

import domain.jr.externalsystems.distance.StationDistance
import domain.jr.externalsystems.distance.StationDistanceRepository
import domain.jr.externalsystems.station.Station
import domain.jr.externalsystems.superexpress.SuperExpress
import domain.jr.externalsystems.util.length.KiloMeter
import domain.jr.externalsystems.util.time.Date
import domain.jr.faresystem.model.basicfare.BasicFare
import domain.jr.faresystem.model.basicfare.BasicFareRepository
import domain.jr.faresystem.model.client.Client
import domain.jr.faresystem.model.fare.Fare
import domain.jr.faresystem.model.situation.Party
import domain.jr.faresystem.model.situation.Situation
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import spock.lang.Specification

import java.time.LocalDate

class FareComputationSpec extends Specification {
    @Mock
    BasicFareRepository basicFareRepository

    @Mock
    SuperExpressSurchargeRepository superExpressSurchargeRepository

    @Mock
    StationDistanceRepository stationDistanceRepository

    @InjectMocks
    FareComputation.FareComputationRepositories repositories

    static Station Tokyo = Mockito.mock(Station.class)
    static Station Shin_Osaka = Mockito.mock(Station.class)
    static Station Himeji = Mockito.mock(Station.class)

    static SuperExpress Hikari = Mockito.mock(SuperExpress.class)
    static SuperExpress Nozomi = Mockito.mock(SuperExpress.class)

    def setup() {
        MockitoAnnotations.openMocks(this)
        Mockito.when(basicFareRepository.getBetween(Tokyo, Shin_Osaka)).thenReturn(Optional.of(new BasicFare(Tokyo, Shin_Osaka, Fare.from(100))))
        Mockito.when(basicFareRepository.getBetween(Tokyo, Himeji)).thenReturn(Optional.of(new BasicFare(Tokyo, Himeji, Fare.from(150))))
        Mockito.when(basicFareRepository.getBetween(Himeji, Tokyo)).thenReturn(Optional.of(new BasicFare(Himeji, Tokyo, Fare.from(150))))

        Mockito.when(superExpressSurchargeRepository.getBetween(Hikari, Tokyo, Shin_Osaka)).thenReturn(Optional.of(new SuperExpressSurcharge(Hikari, Tokyo, Shin_Osaka, Fare.from(1230))))
        Mockito.when(superExpressSurchargeRepository.getBetween(Hikari, Tokyo, Himeji)).thenReturn(Optional.of(new SuperExpressSurcharge(Hikari, Tokyo, Himeji, Fare.from(1230))))
        Mockito.when(superExpressSurchargeRepository.getBetween(Nozomi, Himeji, Tokyo)).thenReturn(Optional.of(new SuperExpressSurcharge(Nozomi, Himeji, Tokyo, Fare.from(1230))))

        Mockito.when(stationDistanceRepository.getBetween(Tokyo, Shin_Osaka)).thenReturn(Optional.of(new StationDistance(Tokyo, Shin_Osaka, KiloMeter.from(100))))
        Mockito.when(stationDistanceRepository.getBetween(Tokyo, Himeji)).thenReturn(Optional.of(new StationDistance(Tokyo, Himeji, KiloMeter.from(801))))
        Mockito.when(stationDistanceRepository.getBetween(Himeji, Tokyo)).thenReturn(Optional.of(new StationDistance(Himeji, Tokyo, KiloMeter.from(801))))
    }

    def "インスタンス生成確認"() {
        expect:
        assert repositories != null
    }

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
