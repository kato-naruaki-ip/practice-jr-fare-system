package com.myexample.practicespring.jr;

import domain.jr.externalsystems.distance.StationDistanceRepository;
import domain.jr.externalsystems.station.StationRepositoryInMemory;
import domain.jr.externalsystems.superexpress.SuperExpressRepositoryInMemory;
import domain.jr.externalsystems.util.time.Date;
import domain.jr.faresystem.model.basicfare.BasicFareRepository;
import domain.jr.faresystem.model.client.Client;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.situation.Party;
import domain.jr.faresystem.model.situation.Situation;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepository;
import domain.jr.faresystem.service.FareComputation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("jr")
public final class FareController {

    private final FareComputation.FareComputationRepositories repositories;

    public FareController(
            BasicFareRepository basicFareRepository,
            SuperExpressSurchargeRepository superExpressSurchargeRepository,
            StationDistanceRepository stationDistanceRepository
    ) {
        this.repositories = new FareComputation.FareComputationRepositories(
                basicFareRepository,
                superExpressSurchargeRepository,
                stationDistanceRepository
        );
    }

    @GetMapping("/sample-case")
    public Map<String, Object> sampleCase() {
        Party party =
                Party.builder(
                                StationRepositoryInMemory.get_姫路駅(),
                                StationRepositoryInMemory.get_東京駅(),
                                Date.from(LocalDate.of(2021, 1, 1)),
                                SuperExpressRepositoryInMemory.get_ひかり()
                        )
                        .client(Client.adult())
                        .isFreeSeat(false)
                        .build();

        Situation situation =
                new Situation.SituationBuilder()
                        ._ゆき(party)
                        .build();

        Fare computed = repositories.toFareComputationWith(situation).totalFare();

        Map<String, Object> result = new HashMap<>();
        result.put("Situation", situation);
        result.put("Party", party);
        result.put("Result", computed.show());
        return result;
    }
}
