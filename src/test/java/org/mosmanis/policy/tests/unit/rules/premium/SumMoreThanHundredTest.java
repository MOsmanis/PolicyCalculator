package org.mosmanis.policy.tests.unit.rules.premium;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.rules.premium.SumMoreThanHundredDoesApply;
import org.mosmanis.policy.enums.PremiumCoefficient;
import org.mosmanis.policy.rules.premium.SumMoreThanHundred;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SumMoreThanHundredTest {
    private static SumMoreThanHundred fireSumMoreThanHundred;

    @BeforeAll
    static void setUp()
    {
        fireSumMoreThanHundred = new SumMoreThanHundred();
    }

    @ParameterizedTest
    @ArgumentsSource(SumMoreThanHundredDoesApply.class)
    void testShouldApply(BigDecimal riskSum, boolean expectedShouldApply)
    {
        assertEquals(expectedShouldApply, fireSumMoreThanHundred.doesApply(riskSum));
    }

    @Test
    void testGetCoefficient()
    {
        BigDecimal expectedCoefficient = BigDecimal.valueOf(PremiumCoefficient.SUM_MORE_THAN_100.coefficient);
        assertEquals(expectedCoefficient, fireSumMoreThanHundred.getCoefficient());
    }
}
