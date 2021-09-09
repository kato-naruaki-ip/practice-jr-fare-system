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
    public void visitFareLeaf(FareTree.FareLeaf node) {
        fare = node.fare;
    }

    @Override
    public void visitBasicFareLeaf(FareTree.BasicFareLeaf node) {
        fare = node.basicFare.getFare();
    }

    @Override
    public void visitSuperExpressSurchargeLeaf(FareTree.SuperExpressSurchargeLeaf node) {
        fare = node.superExpressSurcharge.getFare();
    }

    @Override
    public void visitPlusNode(FareTree.PlusNode node) {
        EvalVisitor visitor1 = zero();
        EvalVisitor visitor2 = zero();

        node.subTree1.accept(visitor1);
        node.subTree1.accept(visitor2);

        Fare fare1 = visitor1.fare;
        Fare fare2 = visitor2.fare;

        fare = fare1._plus_(fare2);
    }

    @Override
    public void visitSumNode(FareTree.SumNode node) {
        Fare result = Fare.from(0);

        for (FareTree ft : node.subTrees) {
            EvalVisitor visitor = zero();

            ft.accept(visitor);
            result = result._plus_(visitor.fare);
        }

        fare = result;
    }

    @Override
    public void visitDiscountNode(FareTree.DiscountNode node) {
        EvalVisitor visitor = zero();

        node.subTree.accept(visitor);

        fare = node.discount.apply(visitor.fare);
    }

    @Override
    public void visitOneChildNode(FareTree.OneChildNode node) {
        EvalVisitor visitorB = zero();
        EvalVisitor visitorS = zero();

        node.basicFare.accept(visitorB);
        node.superExpressSurcharge.accept(visitorS);

        fare = ChildDiscount.computeTotalFareForChild(visitorB.fare, visitorS.fare);
    }
}
