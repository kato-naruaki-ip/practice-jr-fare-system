package domain.jr.faresystem.service;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class AccountVisitor implements FareTree.FareTreeVisitor {
    private static final String HEADER_UNIT = "\t";

    private int depth;
    private List<Tuple2<Integer, String>> lines;

    public static AccountVisitor zero() {
        return new AccountVisitor(0, List.of());
    }

    public String show() {
        return lines.stream()
                .map(line -> HEADER_UNIT.repeat(line._1) + line._2)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void visitFareLeaf(FareTree.FareLeaf node) {
        lines = List.of(Tuple.of(depth, "fare: " + node.fare.show()));
    }

    @Override
    public void visitBasicFareLeaf(FareTree.BasicFareLeaf node) {
        lines = List.of(Tuple.of(depth, "BasicFare: " + node.basicFare.show()));
    }

    @Override
    public void visitSuperExpressSurchargeLeaf(FareTree.SuperExpressSurchargeLeaf node) {
        lines = List.of(Tuple.of(depth, "SuperExpressSurcharge: " + node.superExpressSurcharge.show()));
    }

    @Override
    public void visitPlusNode(FareTree.PlusNode node) {
        AccountVisitor visitor1 = zero();
        AccountVisitor visitor2 = zero();
        visitor1.depth = depth + 1;
        visitor2.depth = depth + 1;

        node.subTree1.accept(visitor1);
        node.subTree2.accept(visitor2);
        var lines1 = visitor1.lines;
        var lines2 = visitor2.lines;

        lines = sum(
                List.of(Tuple.of(depth, "plus: " + EvalVisitor.evaluate(node).show())),
                sum(lines1, lines2)
        );
    }

    @Override
    public void visitSumNode(FareTree.SumNode node) {
        List<List<Tuple2<Integer, String>>> lss = new ArrayList<>();

        for (FareTree ft : node.subTrees) {
            AccountVisitor visitor = zero();
            visitor.depth = depth + 1;

            ft.accept(visitor);
            lss.add(visitor.lines);
        }

        lines = sum(
                List.of(Tuple.of(depth, String.format("sum(%d): %s", lss.size(), EvalVisitor.evaluate(node).show()))),
                lss.stream().reduce(List.of(), AccountVisitor::sum)
        );
    }

    @Override
    public void visitDiscountNode(FareTree.DiscountNode node) {
        AccountVisitor visitor = zero();
        visitor.depth = depth + 1;

        node.subTree.accept(visitor);

        lines = sum(
                List.of(Tuple.of(depth, String.format("Discount(%s): %s", node.discount.getClass().getSimpleName(), EvalVisitor.evaluate(node).show()))),
                visitor.lines
        );
    }

    @Override
    public void visitOneChildNode(FareTree.OneChildNode node) {
        AccountVisitor visitor1 = zero();
        AccountVisitor visitor2 = zero();
        visitor1.depth = depth + 1;
        visitor2.depth = depth + 1;

        node.basicFare.accept(visitor1);
        node.superExpressSurcharge.accept(visitor2);
        var lines1 = visitor1.lines;
        var lines2 = visitor2.lines;

        lines = sum(
                List.of(Tuple.of(depth, "oneChild: " + EvalVisitor.evaluate(node).show())),
                sum(lines1, lines2)
        );
    }

    private static <T> List<T> sum(List<T> l1, List<T> l2) {
        return Stream
                .concat(l1.stream(), l2.stream())
                .collect(Collectors.toUnmodifiableList());
    }
}
