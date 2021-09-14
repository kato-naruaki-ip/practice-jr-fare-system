//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.model.situation

import domain.jr.externalsystems.station.Station
import domain.jr.externalsystems.station.StationRepositoryInMemory
import domain.jr.externalsystems.superexpress.SuperExpress
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory
import domain.jr.externalsystems.util.time.Date
import domain.jr.faresystem.model.client.Client
import domain.jr.faresystem.model.situation.Party.PartyBuilder
import spock.lang.Specification

import java.time.LocalDate

class PartySpec extends Specification {


    static Station demoStation = StationRepositoryInMemory.get_東京駅()
    static Station demoStation2 = StationRepositoryInMemory.get_新大阪駅()

    static SuperExpress demoSuperExpress = SuperExpressRepositoryInMemory.get_ひかり()

    static Date demoDate = Date.from(LocalDate.of(2010, 10, 31))

    PartyBuilder demoBuilder = Party.builder(
            demoStation,
            demoStation2,
            demoDate,
            demoSuperExpress
    )

    def "demoBuilder の動作確認"() {
        when:
        def party = demoBuilder
                .clients(List.of(
                        Client.adult(),
                        Client.adult(),
                        Client.child()
                ))
                .build()

        then:
        assert party.getDeparture() == demoStation
        assert party.getDestination() == demoStation2
        assert party.getDeparture() != party.getDestination()
        assert party.getDate() == demoDate
        assert party.getSuperExpress() == demoSuperExpress
        assert party.getClients().size() == 3
        assert !party.getClients().get(0).isChild()
        assert !party.getClients().get(1).isChild()
        assert party.getClients().get(2).isChild()
        assert !party.isFreeSeat()
    }

    def "null を含む PartyBuilder から build() しようとすると NullPointerException"() {
        when:
        Party.builder(
                departure,
                destination,
                date,
                express
        )
                .build()

        then:
        thrown NullPointerException

        where:
        departure   | destination  | date     | express
        null        | demoStation2 | demoDate | demoSuperExpress
        demoStation | null         | demoDate | demoSuperExpress
        demoStation | demoStation2 | null     | demoSuperExpress
        demoStation | demoStation2 | demoDate | null
        null        | null         | null     | null
    }

    def "clients が null を含むと NullPointerException"() {
        when:
        demoBuilder
                .addClients(clients)
                .build()

        then:
        thrown NullPointerException

        where:
        clients                                                             | _
        new ArrayList<Client>(Arrays.asList((Client) null, Client.adult())) | _
        new ArrayList<Client>(Arrays.asList(Client.adult(), null))          | _
        new ArrayList<Client>(Arrays.asList(Client.child(), null))          | _
    }

    def "clients がゼロ人だと IllegalArgumentException"() {
        when:
        demoBuilder
                .clients(List.of())
                .build()

        then:
        thrown IllegalArgumentException
    }

    def "clients に add() しようとすると UnsupportedOperationException"() {
        given:
        def party =
                demoBuilder
                        .clients(List.of(Client.adult(), Client.adult()))
                        .build()

        when:
        party.getClients().add(Client.adult())

        then:
        thrown UnsupportedOperationException
        assert party.getClients().size() == 2
    }

    def "switchDepartureAndDestination() メソッドの動作確認"() {
        given:
        def builder = demoBuilder.client(Client.adult())
        def party1 = builder.build()
        def party2 = builder.switchDepartureAndDestination().build()

        expect:
        assert party2.getDeparture() == party1.getDestination()
        assert party2.getDestination() == party1.getDeparture()

        assert party1.getDeparture() != party1.getDestination()
    }

    def "addClientRepeatedly() メソッドの動作確認"() {
        given:
        def builder = demoBuilder.client(Client.child())

        when:
        builder.addClientRepeatedly(client, count)

        then:
        assert builder.build().getClients().size() == 1 + count

        where:
        client         | count
        Client.adult() | 0
        Client.adult() | 1
        Client.adult() | 2
        Client.adult() | 3
        Client.child() | 0
        Client.child() | 1
        Client.child() | 2
        Client.child() | 3
    }
}
