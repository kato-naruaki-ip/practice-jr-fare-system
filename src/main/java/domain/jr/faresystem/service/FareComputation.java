package domain.jr.faresystem.service;

import domain.jr.externalsystems.distance.StationDistance;
import domain.jr.externalsystems.distance.StationDistanceRepository;
import domain.jr.faresystem.model.basicfare.BasicFare;
import domain.jr.faresystem.model.basicfare.BasicFareRepository;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.situation.Party;
import domain.jr.faresystem.model.situation.Situation;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurchargeRepository;
import domain.jr.faresystem.service.discount.FreeSeatDiscount;
import domain.jr.faresystem.service.discount.GroupDiscount;
import domain.jr.faresystem.service.discount.RoundTripDiscount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

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
        FareTree fareTree;
        Party party;
        if (situation.is_片道()) {
            party = situation
                    .getParty(Situation.Direction._ゆき)
                    .orElseThrow(AssertionError::new);
            fareTree = computeFare(party);
        } else if (situation.is_往復()) {
            party = situation
                    .getParty(Situation.Direction._ゆき)
                    .orElseThrow(AssertionError::new);
            FareTree ft1 = computeFare(party);

            party = situation
                    .getParty(Situation.Direction._かえり)
                    .orElseThrow(AssertionError::new);
            FareTree ft2 = computeFare(party);

            fareTree =
                    new FareTree.PlusNode(ft1, ft2);
        } else {
            throw new AssertionError();
        }

        EvalVisitor e = new EvalVisitor();
        fareTree.accept(e);
        return e.getFare();
    }

    private FareTree computeFare(Party party) {
        BasicFare basicFare =
                repositories.basicFareRepository
                        .getBetween(party.getDeparture(), party.getDestination())
                        .orElseThrow(RuntimeException::new);

        FareTree.BasicFareLeaf basicFareLeaf =
                new FareTree.BasicFareLeaf(basicFare);

        SuperExpressSurcharge superExpressSurcharge =
                repositories.superExpressSurchargeRepository
                        .getBetween(
                                party.getSuperExpress(),
                                party.getDeparture(),
                                party.getDestination()
                        )
                        .orElseThrow(RuntimeException::new);

        FareTree.SuperExpressSurchargeLeaf superExpressSurchargeLeaf =
                new FareTree.SuperExpressSurchargeLeaf(superExpressSurcharge);

        StationDistance distance =
                repositories.stationDistanceRepository
                        .getBetween(party.getDeparture(), party.getDestination())
                        .orElseThrow(RuntimeException::new);

        FareTree.DiscountNode discountedBasicFare =
                new FareTree.DiscountNode(
                        basicFareLeaf,
                        RoundTripDiscount
                                .when(distance, situation.is_往復())
                );

        FareTree.DiscountNode discountedSuperExpressSurcharge =
                new FareTree.DiscountNode(
                        superExpressSurchargeLeaf,
                        FreeSeatDiscount
                                .when(party.isFreeSeat())
                );

        FareTree.PlusNode oneClient =
                new FareTree.PlusNode(discountedBasicFare, discountedSuperExpressSurcharge);

        FareTree.FareLeaf oneChild =
                new FareTree.FareLeaf(Fare.from(1230)); // TODO

        FareTree.SumNode allClients =
                new FareTree.SumNode(
                        party.getClients().stream()
                                .map(client -> client.isChild() ? oneChild : oneClient)
                                .collect(Collectors.toUnmodifiableList())
                );

        EvalVisitor e = new EvalVisitor();
        oneClient.accept(e);
        Fare fareForOneClient = e.getFare();

        FareTree.DiscountNode discountedAllClients =
                new FareTree.DiscountNode(
                        allClients,
                        GroupDiscount
                                .when(party.getClients().size(), party.getDate(), fareForOneClient)
                );

        return discountedAllClients;
    }
}
