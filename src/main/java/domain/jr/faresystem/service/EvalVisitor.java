package domain.jr.faresystem.service;

import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.service.discount.ChildDiscount;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class EvalVisitor implements FareTree.FareTreeVisitor {
    private Fare acc = Fare.from(0);

    public EvalVisitor() {
    }

    public Fare getFare() {
        return acc;
    }

    @Override
    public void visitFareLeaf(FareTree.FareLeaf current) {
        acc = current.fare;
    }

    @Override
    public void visitBasicFareLeaf(FareTree.BasicFareLeaf current) {
        acc = current.basicFare.getFare();
    }

    @Override
    public void visitSuperExpressSurchargeLeaf(FareTree.SuperExpressSurchargeLeaf current) {
        acc = current.superExpressSurcharge.getFare();
    }

    @Override
    public void visitPlusNode(FareTree.PlusNode current) {
        current.ft1.accept(this);
        var tmp1 = acc;
        current.ft2.accept(this);
        var tmp2 = acc;

        acc = tmp1._plus_(tmp2);
    }

    @Override
    public void visitSumNode(FareTree.SumNode current) {
        Fare result = Fare.from(0);

        for (FareTree ft : current.fts) {
            ft.accept(this);
            result = result._plus_(acc);
        }

        acc = result;
    }

    @Override
    public void visitDiscountNode(FareTree.DiscountNode current) {
        current.ft.accept(this);

        acc = current.discount.apply(acc);
    }

    @Override
    public void visitOneChildNode(FareTree.OneChildNode current) {
        current.basicFare.accept(this);
        var tmp1 = acc;
        current.superExpressSurcharge.accept(this);
        var tmp2 = acc;

        acc = ChildDiscount.computeTotalFareForChild(tmp1, tmp2);
    }
}
