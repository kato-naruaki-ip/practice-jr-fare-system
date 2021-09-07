package domain.jr.faresystem.service;

import domain.jr.externalsystems.distance.StationDistance;
import domain.jr.externalsystems.distance.StationDistanceRepository;
import domain.jr.faresystem.model.basicfare.BasicFare;
import domain.jr.faresystem.model.basicfare.BasicFareRepository;
import domain.jr.faresystem.model.client.Client;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.situation.Party;
import domain.jr.faresystem.model.situation.Situation;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepository;
import domain.jr.faresystem.service.discount.ChildDiscount;
import domain.jr.faresystem.service.discount.FreeSeatDiscount;
import domain.jr.faresystem.service.discount.GroupDiscount;
import domain.jr.faresystem.service.discount.RoundTripDiscount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public final class FareComputation {
    private final FareComputationRepositories repositories;
    private final Situation situation;

    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class FareComputationRepositories {
        private final BasicFareRepository basicFareRepository;
        private final SuperExpressSurchargeRepository superExpressSurchargeRepository;
        private final StationDistanceRepository stationDistanceRepository;

        public FareComputation toFareComputationWith(Situation situation) {
            return new FareComputation(this, situation);
        }
    }

    private FareComputation(
            FareComputationRepositories repositories,
            Situation situation
    ) {
        this.repositories = repositories;
        this.situation = situation;
    }

    public Fare totalFare() {
        Fare result = Fare.from(0);

        Party party;
        if (situation.is_片道()) {
            party = situation.getParty(Situation.Direction._ゆき).orElseThrow(AssertionError::new);
            result = result._plus_(computeFare(party));
        } else if (situation.is_往復()) {
            party = situation.getParty(Situation.Direction._ゆき).orElseThrow(AssertionError::new);
            result = result._plus_(computeFare(party));

            party = situation.getParty(Situation.Direction._かえり).orElseThrow(AssertionError::new);
            result = result._plus_(computeFare(party));
        } else {
            throw new AssertionError();
        }

        return result;
    }

    private Fare computeFare(Party party) {
        Fare basicFare =
                repositories.basicFareRepository
                        .getBetween(party.getDeparture(), party.getDestination())
                        .map(BasicFare::getFare)
                        .orElseThrow(RuntimeException::new);

        Fare superExpressSurchargeFare =
                repositories.superExpressSurchargeRepository
                        .getBetween(
                                party.getSuperExpress(),
                                party.getDeparture(),
                                party.getDestination()
                        )
                        .map(SuperExpressSurcharge::getFare)
                        .orElseThrow(RuntimeException::new);

        StationDistance distance = repositories.stationDistanceRepository
                .getBetween(party.getDeparture(), party.getDestination())
                .orElseThrow(RuntimeException::new);

        basicFare =
                RoundTripDiscount
                        .when(distance, situation.is_往復())
                        .apply(basicFare);

        superExpressSurchargeFare =
                FreeSeatDiscount
                        .when(party.isFreeSeat())
                        .apply(superExpressSurchargeFare);

        Fare oneClient = basicFare._plus_(superExpressSurchargeFare);
        Fare oneChild = ChildDiscount.computeTotalFareForChild(basicFare, superExpressSurchargeFare);

        Fare result = Fare.from(0);

        for (Client client : party.getClients()) {
            if (client.isChild()) {
                result = result._plus_(oneChild);
            } else {
                result = result._plus_(oneClient);
            }
        }

        result =
                GroupDiscount
                        .when(party.getClients().size(), party.getDate(), oneClient)
                        .apply(result);

        return result;
    }

}
