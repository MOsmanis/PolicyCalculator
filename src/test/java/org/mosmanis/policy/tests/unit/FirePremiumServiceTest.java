package org.mosmanis.policy.tests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.PremiumServiceGetCoefficientExceptions;
import org.mosmanis.policy.argumentsproviders.unit.PremiumServiceGetCoefficientValues;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.rules.premium.PremiumRule;
import org.mosmanis.policy.services.FirePremiumService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FirePremiumServiceTest {
    @Test
    void testRiskType()
    {
        var testablePremiumService = new FirePremiumService(new ArrayList<>());
        assertEquals(RiskType.FIRE, testablePremiumService.getRiskType());
    }

    @ParameterizedTest
    @ArgumentsSource(PremiumServiceGetCoefficientExceptions.class)
    void testGetCoefficient(Collection<PremiumRule> mockedPremiumRules, BigDecimal testRiskSum,
                                      Class<? extends Exception> expectedException)
    {
        var testablePremiumService = new FirePremiumService(mockedPremiumRules);
        assertThrows(expectedException, () -> testablePremiumService.getCoefficient(testRiskSum));
    }

    @ParameterizedTest
    @ArgumentsSource(PremiumServiceGetCoefficientValues.class)
    void testGetCoefficient(Collection<PremiumRule> mockPremiumRules,
                            BigDecimal testRiskSum, BigDecimal expectedCoefficient)
    {
        var testablePremiumService = new FirePremiumService(mockPremiumRules);

        BigDecimal actualCoefficient =
            assertDoesNotThrow(() -> testablePremiumService.getCoefficient(testRiskSum));

        assertEquals(expectedCoefficient, actualCoefficient);
    }
}
