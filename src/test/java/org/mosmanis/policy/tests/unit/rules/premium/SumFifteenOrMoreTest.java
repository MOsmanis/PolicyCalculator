package org.mosmanis.policy.tests.unit.rules.premium;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.rules.premium.SumFifteenOrMoreDoesApply;
import org.mosmanis.policy.enums.PremiumCoefficient;
import org.mosmanis.policy.rules.premium.SumFifteenOrMore;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SumFifteenOrMoreTest {

    private static SumFifteenOrMore sumFifteenOrMore;

    @BeforeAll
    static void setUp()
    {
        sumFifteenOrMore = new SumFifteenOrMore();
    }

    @ParameterizedTest
    @ArgumentsSource(SumFifteenOrMoreDoesApply.class)
    void testShouldApply(BigDecimal riskSum, boolean expectedShouldApply)
    {
        assertEquals(expectedShouldApply, sumFifteenOrMore.doesApply(riskSum));
    }

    @Test
    void testGetCoefficient()
    {
        BigDecimal expectedCoefficient = BigDecimal.valueOf(PremiumCoefficient.SUM_15_OR_MORE.coefficient);
        assertEquals(expectedCoefficient, sumFifteenOrMore.getCoefficient());
    }
}
