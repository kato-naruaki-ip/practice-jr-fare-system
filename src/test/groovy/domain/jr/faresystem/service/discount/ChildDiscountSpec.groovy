//file:noinspection NonAsciiCharacters
package domain.jr.faresystem.service.discount

import domain.jr.faresystem.model.client.Client
import domain.jr.faresystem.model.fare.Fare
import spock.lang.Specification

class ChildDiscountSpec extends Specification {

    static Client ADULT = Client.adult()
    static Client CHILD = Client.child()

    def "コンストラクタの動作確認"() {
        when:
        ChildDiscount discount = ChildDiscount.when(client)

        then:
        assert discount.isChild() == client.isChild()

        where:
        client << [ADULT, CHILD]
    }

    def "合計運賃の計算確認: #basicFare.show(), #superExpressFare.show() => #expectedFare.show()"() {
        when:
        def fare = ChildDiscount.computeTotalFareForChild(basicFare, superExpressFare)

        then:
        assert fare == expectedFare

        where:
        basic | superExpress || expected
        10_000 | 5_000        || 7500
        10_010 | 5_000        || 7500
        10_020 | 5_000        || 7510

        10_000 | 5_010        || 7500
        10_010 | 5_010        || 7500
        10_020 | 5_010        || 7510

        10_000 | 5_020        || 7510
        10_010 | 5_020        || 7510
        10_020 | 5_020        || 7520

        basicFare = Fare.from(basic)
        superExpressFare = Fare.from(superExpress)
        expectedFare = Fare.from(expected)
    }

    def "適用確認: (#client) #value => #expectedValue"() {
        when:
        def discount = ChildDiscount.when(client)
        def actual = discount.apply(fare)

        then:
        assert actual == expected

        where:
        client | value  || expectedValue
        ADULT  | 10_000 || 10_000
        ADULT  | 10_010 || 10_010
        ADULT  | 10_020 || 10_020
        CHILD  | 10_000 || 5_000
        CHILD  | 10_010 || 5_000
        CHILD  | 10_020 || 5_010

        fare = Fare.from(value)
        expected = Fare.from(expectedValue)
    }
}
