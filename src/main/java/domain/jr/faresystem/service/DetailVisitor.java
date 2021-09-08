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
    private int currentDepth;
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
    public void visitFareLeaf(FareTree.FareLeaf current) {
        fare = current.fare;
        lines = List.of(Tuple.of(currentDepth, "fare: " + current.fare.show()));
    }

    @Override
    public void visitBasicFareLeaf(FareTree.BasicFareLeaf current) {
        fare = current.basicFare.getFare();
        lines = List.of(Tuple.of(currentDepth, "BasicFare: " + current.basicFare.toString()));
    }

    @Override
    public void visitSuperExpressSurchargeLeaf(FareTree.SuperExpressSurchargeLeaf current) {
        fare = current.superExpressSurcharge.getFare();
        lines = List.of(Tuple.of(currentDepth, "SuperExpressSurcharge: " + current.superExpressSurcharge.toString()));
    }

    @Override
    public void visitPlusNode(FareTree.PlusNode current) {
        DetailVisitor visitor1 = zero();
        DetailVisitor visitor2 = zero();
        visitor1.currentDepth = currentDepth + 1;
        visitor2.currentDepth = currentDepth + 1;

        current.ft1.accept(visitor1);
        current.ft2.accept(visitor2);
        var fare1 = visitor1.fare;
        var fare2 = visitor2.fare;
        var lines1 = visitor1.lines;
        var lines2 = visitor2.lines;

        fare = fare1._plus_(fare2);
        lines = sum(
                List.of(Tuple.of(currentDepth, "plus: " + fare.show())),
                sum(lines1, lines2)
        );
    }

    @Override
    public void visitSumNode(FareTree.SumNode current) {
        List<Fare> results = new ArrayList<>();
        List<List<Tuple2<Integer, String>>> lss = new ArrayList<>();

        for (FareTree ft : current.fts) {
            DetailVisitor visitor = zero();
            visitor.currentDepth = currentDepth + 1;

            ft.accept(visitor);
            results.add(visitor.fare);
            lss.add(visitor.lines);
        }

        fare = results.stream()
                .reduce(Fare.from(0), Fare::_plus_);
        lines = sum(
                List.of(Tuple.of(currentDepth, String.format("sum(%d): %s", lss.size(), fare.show()))),
                lss.stream().reduce(List.of(), DetailVisitor::sum)
        );
    }

    @Override
    public void visitDiscountNode(FareTree.DiscountNode current) {
        DetailVisitor visitor = zero();
        visitor.currentDepth = currentDepth + 1;

        current.ft.accept(visitor);

        fare = current.discount.apply(visitor.fare);
        lines = sum(
                List.of(Tuple.of(currentDepth, String.format("Discount(%s): %s", current.discount.getClass().getSimpleName(), fare.show()))),
                visitor.lines
        );
    }

    @Override
    public void visitOneChildNode(FareTree.OneChildNode current) {
        DetailVisitor visitor1 = zero();
        DetailVisitor visitor2 = zero();
        visitor1.currentDepth = currentDepth + 1;
        visitor2.currentDepth = currentDepth + 1;

        current.basicFare.accept(visitor1);
        current.superExpressSurcharge.accept(visitor2);
        var fare1 = visitor1.fare;
        var fare2 = visitor2.fare;
        var lines1 = visitor1.lines;
        var lines2 = visitor2.lines;

        fare = ChildDiscount.computeTotalFareForChild(fare1, fare2);
        lines = sum(
                List.of(Tuple.of(currentDepth, "oneChild: " + fare.show())),
                sum(lines1, lines2)
        );
    }

    private static <T> List<T> sum(List<T> l1, List<T> l2) {
        return Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toUnmodifiableList());
    }
}
