package org.mosmanis.policy.tests.unit.rules.premium;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.rules.premium.SumLessThanFifteenDoesApply;
import org.mosmanis.policy.enums.PremiumCoefficient;
import org.mosmanis.policy.rules.premium.SumLessThanFifteen;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SumLessThanFifteenTest {

    private static SumLessThanFifteen sumLessThanFifteen;

    @BeforeAll
    static void setUp()
    {
        sumLessThanFifteen = new SumLessThanFifteen();
    }

    @ParameterizedTest
    @ArgumentsSource(SumLessThanFifteenDoesApply.class)
    void testShouldApply(BigDecimal riskSum, boolean expectedShouldApply)
    {
        assertEquals(expectedShouldApply, sumLessThanFifteen.doesApply(riskSum));
    }

    @Test
    void testGetCoefficient()
    {
        BigDecimal expectedCoefficient = BigDecimal.valueOf(PremiumCoefficient.SUM_LESS_THAN_15.coefficient);
        assertEquals(expectedCoefficient, sumLessThanFifteen.getCoefficient());
    }
}
