package domain.jr.faresystem.service;

import domain.jr.faresystem.model.basicfare.BasicFare;
import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.model.surcharge.superexpress.SuperExpressSurcharge;
import domain.jr.faresystem.service.discount.Discount;
import lombok.AllArgsConstructor;

import java.util.List;

public abstract class FareTree {
    abstract public void accept(FareTreeVisitor visitor);

    public Fare toTotalFare() {
        return EvalVisitor.evaluate(this);
    }

    public String toAccountString() {
        return AccountVisitor.convertToString(this);
    }

    public interface FareTreeVisitor {
        void visitFareLeaf(FareLeaf node);

        void visitBasicFareLeaf(BasicFareLeaf node);

        void visitSuperExpressSurchargeLeaf(SuperExpressSurchargeLeaf node);

        void visitPlusNode(PlusNode node);

        void visitSumNode(SumNode node);

        void visitDiscountNode(DiscountNode node);

        void visitOneChildNode(OneChildNode node);
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
        final FareTree subTree1;
        final FareTree subTree2;

        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitPlusNode(this);
        }
    }

    @AllArgsConstructor
    public static class SumNode extends FareTree {
        final List<FareTree> subTrees;

        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitSumNode(this);
        }
    }

    @AllArgsConstructor
    public static class DiscountNode extends FareTree {
        final FareTree subTree;
        final Discount discount;

        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitDiscountNode(this);
        }
    }

    @AllArgsConstructor
    public static class OneChildNode extends FareTree {
        final FareTree basicFare;
        final FareTree superExpressSurcharge;

        @Override
        public void accept(FareTreeVisitor visitor) {
            visitor.visitOneChildNode(this);
        }
    }
}
