package org.mosmanis.policy.argumentsproviders.integration;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mosmanis.policy.services.TestPolicyService;

import java.util.stream.Stream;

public class PremiumCalculatorCalculateValues implements ArgumentsProvider {
    private final TestPolicyService testPolicyService;

    public PremiumCalculatorCalculateValues()
    {
        this.testPolicyService = new TestPolicyService();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            Arguments.of(this.testPolicyService.getEmptyPolicy(), 0),
            Arguments.of(this.testPolicyService.getFirstAcceptanceCriteriaPolicy(), 2.28),
            Arguments.of(this.testPolicyService.getSecondAcceptanceCriteriaPolicy(), 17.13),
            getArgumentsForLargeAmountPolicyObjects()
        );
    }

    /**
     * expectedPremium = firePremium + theftPremium = n * riskSum * fireCoefficient + n * riskSum * theftCoefficient
     * 7325.93 (7325.92674) = 2375.97624 + 4949.9505 = 99 * 999.99 * 0.024 + 99 * 999.99 * 0.05
     */
    private Arguments getArgumentsForLargeAmountPolicyObjects()
    {
        return Arguments.of(
            this.testPolicyService.getPolicyWithMultipleObjectsAndAllRiskTypes(99, 999.99),
            7325.93
        );
    }
}
