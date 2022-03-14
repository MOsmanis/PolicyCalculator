package org.mosmanis.policy.tests.unit.rules.premium;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.rules.premium.SumHundredOrLessDoesApply;
import org.mosmanis.policy.enums.PremiumCoefficient;
import org.mosmanis.policy.rules.premium.SumHundredOrLess;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SumHundredOrLessTest {
    private static SumHundredOrLess sumHundredOrLess;

    @BeforeAll
    static void setUp()
    {
        sumHundredOrLess = new SumHundredOrLess();
    }

    @ParameterizedTest
    @ArgumentsSource(SumHundredOrLessDoesApply.class)
    void testShouldApply(BigDecimal riskSum, boolean expectedShouldApply)
    {
        assertEquals(expectedShouldApply, sumHundredOrLess.doesApply(riskSum));
    }

    @Test
    void testGetCoefficient()
    {
        BigDecimal expectedCoefficient = BigDecimal.valueOf(PremiumCoefficient.SUM_100_OR_LESS.coefficient);
        assertEquals(expectedCoefficient, sumHundredOrLess.getCoefficient());
    }
}
