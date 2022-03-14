package org.mosmanis.policy.tests.unit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.PremiumCalculatorCalculateExceptions;
import org.mosmanis.policy.argumentsproviders.unit.PremiumCalculatorCalculateValues;
import org.mosmanis.policy.calculators.PremiumCalculator;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.exceptions.PolicyCalculationException;
import org.mosmanis.policy.factories.PremiumServiceFactory;
import org.mosmanis.policy.services.PolicyService;
import org.mosmanis.policy.validators.PolicyValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PremiumCalculatorTest {
    @ParameterizedTest
    @ArgumentsSource(PremiumCalculatorCalculateValues.class)
    void testCalculate(Policy policy, double expectedPremium, PremiumServiceFactory mockedPremiumServiceFactory,
                       PolicyService mockedPolicyService, PolicyValidator mockedPolicyValidator) throws Exception
    {
        var premiumCalculator =
            new PremiumCalculator(mockedPremiumServiceFactory, mockedPolicyService, mockedPolicyValidator);
        double actualPremium = premiumCalculator.calculate(policy);
        assertEquals(expectedPremium, actualPremium);
    }

    @ParameterizedTest
    @ArgumentsSource(PremiumCalculatorCalculateExceptions.class)
    void testCalculate(Policy policy, PremiumServiceFactory mockedPremiumServiceFactory,
                       PolicyService mockedPolicyService, PolicyValidator mockedPolicyValidator,
                       Class<? extends PolicyCalculationException> expectedException)
    {
        var premiumCalculator =
            new PremiumCalculator(mockedPremiumServiceFactory, mockedPolicyService, mockedPolicyValidator);
        assertThrows(expectedException, () -> premiumCalculator.calculate(policy));
    }
}
