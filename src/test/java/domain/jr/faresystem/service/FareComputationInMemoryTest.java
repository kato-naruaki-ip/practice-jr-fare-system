package domain.jr.faresystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.jr.externalsystems.distance.StationDistanceRepositoryInMemory;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory;
import domain.jr.externalsystems.util.time.Date;
import domain.jr.faresystem.model.basicfare.BasicFareRepositoryInMemory;
import domain.jr.faresystem.model.client.Client;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.situation.Party;
import domain.jr.faresystem.model.situation.Situation;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepositoryInMemory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FareComputationInMemoryTest {
    private final FareComputation.FareComputationRepositories repositories = new FareComputation.FareComputationRepositories(
            new BasicFareRepositoryInMemory(),
            new SuperExpressSurchargeRepositoryInMemory(),
            new StationDistanceRepositoryInMemory()
    );

    private Situation situation;
    private Party party;
    private Fare result;

    @AfterEach
    void printResults() {
        System.out.println("入力:");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            System.out.println(mapper.writeValueAsString(situation));
            System.out.println(mapper.writeValueAsString(party));
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        System.out.printf(">>> 合計料金は%sです\n", result.getYen().show());
    }

    @Test
    void sampleCase_1() {
        party =
                Party.builder(
                                StationRepositoryInMemory.get_東京駅(),
                                StationRepositoryInMemory.get_新大阪駅(),
                                Date.from(LocalDate.of(2021, 12, 31)),
                                SuperExpressRepositoryInMemory.get_のぞみ()
                        )
                        .addClients(List.of(
                                Client.adult(),
                                Client.adult(),
                                Client.child(),
                                Client.adult(),
                                Client.child(),
                                Client.adult(),
                                Client.child(),
                                Client.adult(),
                                Client.child(),
                                Client.adult(),
                                Client.child()
                        ))
                        .addClientRepeatedly(Client.adult(), 100)
                        .isFreeSeat(false)
                        .build();

        situation = new Situation.SituationBuilder()
                ._ゆき(party)
                .build();

        result = repositories.toFareComputationWith(situation).totalFare();

        assertTrue(true);
    }

    @Test
    void sampleCase_2() {
        Party party =
                Party.builder(
                                StationRepositoryInMemory.get_東京駅(),
                                StationRepositoryInMemory.get_新大阪駅(),
                                Date.from(LocalDate.of(2021, 1, 1)),
                                SuperExpressRepositoryInMemory.get_のぞみ()
                        )
                        .addClients(List.of(
                                Client.adult(),
                                Client.adult(),
                                Client.child()
                        ))
                        .isFreeSeat(false)
                        .build();

        situation = new Situation.SituationBuilder()
                ._ゆき(
                        party
                )
                ._かえり(
                        party.toBuilder()
                                .switchDepartureAndDestination()
                                .date(Date.from(LocalDate.of(2021, 1, 3)))
                                .superExpress(SuperExpressRepositoryInMemory.get_ひかり())
                                .build()
                )
                .build();

        result = repositories.toFareComputationWith(situation).totalFare();

        assertTrue(true);
    }

    @Test
    void sampleCase_3() {
        party =
                Party.builder(
                                StationRepositoryInMemory.get_姫路駅(),
                                StationRepositoryInMemory.get_東京駅(),
                                Date.from(LocalDate.of(2021, 1, 1)),
                                SuperExpressRepositoryInMemory.get_ひかり()
                        )
                        .client(Client.adult())
                        .isFreeSeat(false)
                        .build();

        situation = new Situation.SituationBuilder()
                ._ゆき(party)
                .build();

        result = repositories.toFareComputationWith(situation).totalFare();

        assertTrue(true);
    }
}
