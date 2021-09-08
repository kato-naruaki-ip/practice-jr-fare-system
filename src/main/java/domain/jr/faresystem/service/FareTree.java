package domain.jr.faresystem.service;

import domain.jr.faresystem.model.basicfare.BasicFare;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import domain.jr.faresystem.service.discount.Discount;
import lombok.AllArgsConstructor;

import java.util.List;

public abstract class FareTree {
    abstract public void accept(FareTreeVisitor visitor);

    public interface FareTreeVisitor {
        void visitFareLeaf(FareLeaf current);
        void visitBasicFareLeaf(BasicFareLeaf current);
        void visitSuperExpressSurchargeLeaf(SuperExpressSurchargeLeaf current);

        void visitPlusNode(PlusNode current);
        void visitSumNode(SumNode current);
        void visitDiscountNode(DiscountNode current);
    }

    @AllArgsConstructor
    public static class FareLeaf extends FareTree {
        final Fare fare;
        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitFareLeaf(this);
        }
    }

    @AllArgsConstructor
    public static class BasicFareLeaf extends FareTree {
        final BasicFare basicFare;
        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitBasicFareLeaf(this);
        }
    }

    @AllArgsConstructor
    public static class SuperExpressSurchargeLeaf extends FareTree {
        final SuperExpressSurcharge superExpressSurcharge;
        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitSuperExpressSurchargeLeaf(this);
        }
    }

    @AllArgsConstructor
    public static class PlusNode extends FareTree {
        final FareTree ft1;
        final FareTree ft2;
        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitPlusNode(this);
        }
    }

    @AllArgsConstructor
    public static class SumNode extends FareTree {
        final List<FareTree> fts;
        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitSumNode(this);
        }
    }

    @AllArgsConstructor
    public static class DiscountNode extends FareTree {
        final FareTree ft;
        final Discount discount;
        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitDiscountNode(this);
        }
    }

}
