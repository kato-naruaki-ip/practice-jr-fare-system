package domain.jr.faresystem.service;

import domain.jr.faresystem.model.fare.Fare;
import domain.jr.faresystem.service.discount.ChildDiscount;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class DetailVisitor implements FareTree.FareTreeVisitor {
    private static final String HEADER_UNIT = "\t";

    private Fare fare;
    private int depth;
    private List<Tuple2<Integer, String>> lines;

    public static DetailVisitor zero() {
        return new DetailVisitor(Fare.from(0), 0, List.of());
    }

    public Fare getFare() {
        return fare;
    }

    public String show() {
        return lines.stream()
                .map(line -> HEADER_UNIT.repeat(line._1) + line._2)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void visitFareLeaf(FareTree.FareLeaf node) {
        fare = node.fare;
        lines = List.of(Tuple.of(depth, "fare: " + node.fare.show()));
    }

    @Override
    public void visitBasicFareLeaf(FareTree.BasicFareLeaf node) {
        fare = node.basicFare.getFare();
        lines = List.of(Tuple.of(depth, "BasicFare: " + node.basicFare.show()));
    }

    @Override
    public void visitSuperExpressSurchargeLeaf(FareTree.SuperExpressSurchargeLeaf node) {
        fare = node.superExpressSurcharge.getFare();
        lines = List.of(Tuple.of(depth, "SuperExpressSurcharge: " + node.superExpressSurcharge.show()));
    }

    @Override
    public void visitPlusNode(FareTree.PlusNode node) {
        DetailVisitor visitor1 = zero();
        DetailVisitor visitor2 = zero();
        visitor1.depth = depth + 1;
        visitor2.depth = depth + 1;

        node.subTree1.accept(visitor1);
        node.subTree2.accept(visitor2);
        var fare1 = visitor1.fare;
        var fare2 = visitor2.fare;
        var lines1 = visitor1.lines;
        var lines2 = visitor2.lines;

        fare = fare1._plus_(fare2);
        lines = sum(
                List.of(Tuple.of(depth, "plus: " + fare.show())),
                sum(lines1, lines2)
        );
    }

    @Override
    public void visitSumNode(FareTree.SumNode node) {
        List<Fare> results = new ArrayList<>();
        List<List<Tuple2<Integer, String>>> lss = new ArrayList<>();

        for (FareTree ft : node.subTrees) {
            DetailVisitor visitor = zero();
            visitor.depth = depth + 1;

            ft.accept(visitor);
            results.add(visitor.fare);
            lss.add(visitor.lines);
        }

        fare = results.stream()
                .reduce(Fare.from(0), Fare::_plus_);
        lines = sum(
                List.of(Tuple.of(depth, String.format("sum(%d): %s", lss.size(), fare.show()))),
                lss.stream().reduce(List.of(), DetailVisitor::sum)
        );
    }

    @Override
    public void visitDiscountNode(FareTree.DiscountNode node) {
        DetailVisitor visitor = zero();
        visitor.depth = depth + 1;

        node.subTree.accept(visitor);

        fare = node.discount.apply(visitor.fare);
        lines = sum(
                List.of(Tuple.of(depth, String.format("Discount(%s): %s", node.discount.getClass().getSimpleName(), fare.show()))),
                visitor.lines
        );
    }

    @Override
    public void visitOneChildNode(FareTree.OneChildNode node) {
        DetailVisitor visitor1 = zero();
        DetailVisitor visitor2 = zero();
        visitor1.depth = depth + 1;
        visitor2.depth = depth + 1;

        node.basicFare.accept(visitor1);
        node.superExpressSurcharge.accept(visitor2);
        var fare1 = visitor1.fare;
        var fare2 = visitor2.fare;
        var lines1 = visitor1.lines;
        var lines2 = visitor2.lines;

        fare = ChildDiscount.computeTotalFareForChild(fare1, fare2);
        lines = sum(
                List.of(Tuple.of(depth, "oneChild: " + fare.show())),
                sum(lines1, lines2)
        );
    }

    private static <T> List<T> sum(List<T> l1, List<T> l2) {
        return Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toUnmodifiableList());
    }
}
