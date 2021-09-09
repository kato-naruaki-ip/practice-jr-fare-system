package domain.jr.faresystem.service;

import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.service.discount.ChildDiscount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class EvalVisitor implements FareTree.FareTreeVisitor {
    private Fare fare;

    public static EvalVisitor zero() {
        return new EvalVisitor(Fare.from(0));
    }

    public Fare getFare() {
        return fare;
    }

    public static Fare evaluate(FareTree fareTree) {
        EvalVisitor visitor = zero();

        fareTree.accept(visitor);

        return visitor.getFare();
    }

    @Override
    public void visitFareLeaf(FareTree.FareLeaf current) {
        fare = current.fare;
    }

    @Override
    public void visitBasicFareLeaf(FareTree.BasicFareLeaf current) {
        fare = current.basicFare.getFare();
    }

    @Override
    public void visitSuperExpressSurchargeLeaf(FareTree.SuperExpressSurchargeLeaf current) {
        fare = current.superExpressSurcharge.getFare();
    }

    @Override
    public void visitPlusNode(FareTree.PlusNode current) {
        EvalVisitor visitor1 = zero();
        EvalVisitor visitor2 = zero();

        current.ft1.accept(visitor1);
        current.ft1.accept(visitor2);

        Fare fare1 = visitor1.fare;
        Fare fare2 = visitor2.fare;

        fare = fare1._plus_(fare2);
    }

    @Override
    public void visitSumNode(FareTree.SumNode current) {
        Fare result = Fare.from(0);

        for (FareTree ft : current.fts) {
            EvalVisitor visitor = zero();

            ft.accept(visitor);
            result = result._plus_(visitor.fare);
        }

        fare = result;
    }

    @Override
    public void visitDiscountNode(FareTree.DiscountNode current) {
        EvalVisitor visitor = zero();

        current.ft.accept(visitor);

        fare = current.discount.apply(visitor.fare);
    }

    @Override
    public void visitOneChildNode(FareTree.OneChildNode current) {
        EvalVisitor visitorB = zero();
        EvalVisitor visitorS = zero();

        current.basicFare.accept(visitorB);
        current.superExpressSurcharge.accept(visitorS);

        fare = ChildDiscount.computeTotalFareForChild(visitorB.fare, visitorS.fare);
    }
}
