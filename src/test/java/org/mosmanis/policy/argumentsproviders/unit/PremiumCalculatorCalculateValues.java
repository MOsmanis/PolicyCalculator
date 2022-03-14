package org.mosmanis.policy.argumentsproviders.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mockito.Mockito;
import org.mosmanis.policy.builders.PolicySubObjectBuilder;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.exceptions.*;
import org.mosmanis.policy.factories.PremiumServiceFactory;
import org.mosmanis.policy.services.FirePremiumService;
import org.mosmanis.policy.services.PolicyService;
import org.mosmanis.policy.services.TestPolicyService;
import org.mosmanis.policy.services.TheftPremiumService;
import org.mosmanis.policy.validators.PolicyValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
            Arguments.of(this.testPolicyService.getEmptyPolicy(), 0,
                mockPremiumServiceFactoryForEmptyPolicy(),
                mockPolicyServiceForEmptyPolicy(),
                Mockito.mock(PolicyValidator.class)),
            Arguments.of(this.testPolicyService.getFirstAcceptanceCriteriaPolicy(), 2.28,
                mockPremiumServiceFactoryForFirstAcceptanceCriteria(),
                mockPolicyServiceForFirstAcceptanceCriteria(),
                Mockito.mock(PolicyValidator.class)),
            Arguments.of(this.testPolicyService.getSecondAcceptanceCriteriaPolicy(), 17.13,
                mockPremiumServiceFactoryForSecondAcceptanceCriteria(),
                mockPolicyServiceForSecondAcceptanceCriteria(),
                Mockito.mock(PolicyValidator.class)),
            getArgumentsForLargeAmountPolicyObjects()
        );
    }

    private static PremiumServiceFactory mockPremiumServiceFactoryForEmptyPolicy()
    {
        return mock(PremiumServiceFactory.class);
    }

    private static PolicyService mockPolicyServiceForEmptyPolicy()
    {
        PolicyService policyService = mock(PolicyService.class);
        when(policyService.getSubObjectsByRiskType(any())).thenReturn(new HashMap<>());
        return policyService;
    }

    private static PremiumServiceFactory mockPremiumServiceFactoryForFirstAcceptanceCriteria()
        throws PremiumCoefficientException, PremiumServiceNotDefinedException
    {
        PremiumServiceFactory premiumServiceFactory = mock(PremiumServiceFactory.class);
        addFirePremiumService(premiumServiceFactory, BigDecimal.valueOf(100.0), BigDecimal.valueOf(0.014));
        addTheftPremiumService(premiumServiceFactory, BigDecimal.valueOf(8.0), BigDecimal.valueOf(0.11));
        return premiumServiceFactory;
    }

    private static PolicyService mockPolicyServiceForFirstAcceptanceCriteria()
    {
        PolicyService policyService = mock(PolicyService.class);
        var subObjectsByRiskType = new HashMap<RiskType, List<PolicySubObject>>();
        subObjectsByRiskType.put(RiskType.FIRE,
            List.of(new PolicySubObjectBuilder(RiskType.FIRE).withSum(100.0).make()));
        subObjectsByRiskType.put(RiskType.THEFT,
            List.of(new PolicySubObjectBuilder(RiskType.THEFT).withSum(8.0).make()));
        when(policyService.getSubObjectsByRiskType(any())).thenReturn(subObjectsByRiskType);

        return policyService;
    }

    private static PremiumServiceFactory mockPremiumServiceFactoryForSecondAcceptanceCriteria()
        throws PremiumCoefficientException, PremiumServiceNotDefinedException
    {
        PremiumServiceFactory premiumServiceFactory = mock(PremiumServiceFactory.class);
        addFirePremiumService(premiumServiceFactory, BigDecimal.valueOf(500.0), BigDecimal.valueOf(0.024));
        addTheftPremiumService(premiumServiceFactory, BigDecimal.valueOf(102.51), BigDecimal.valueOf(0.05));
        return premiumServiceFactory;
    }

    private static PolicyService mockPolicyServiceForSecondAcceptanceCriteria()
    {
        PolicyService policyService = mock(PolicyService.class);
        var subObjectsByRiskType = new HashMap<RiskType, List<PolicySubObject>>();
        subObjectsByRiskType.put(RiskType.FIRE,
            List.of(new PolicySubObjectBuilder(RiskType.FIRE).withSum(500.0).make()));
        subObjectsByRiskType.put(RiskType.THEFT,
            List.of(new PolicySubObjectBuilder(RiskType.THEFT).withSum(102.51).make()));
        when(policyService.getSubObjectsByRiskType(any())).thenReturn(subObjectsByRiskType);

        return policyService;
    }

    private static void addFirePremiumService(PremiumServiceFactory premiumServiceFactory,
                                              BigDecimal riskSum, BigDecimal coefficient)
        throws PremiumCoefficientException, PremiumServiceNotDefinedException
    {
        FirePremiumService firePremiumService = mockFirePremiumService(riskSum, coefficient);
        when(premiumServiceFactory.get(RiskType.FIRE)).thenReturn(firePremiumService);
    }

    private static FirePremiumService mockFirePremiumService(BigDecimal riskSum, BigDecimal coefficient)
        throws PremiumRuleNotFoundException, MultiplePremiumRulesFoundException
    {
        FirePremiumService firePremiumService = mock(FirePremiumService.class);
        when(firePremiumService.getCoefficient(riskSum)).thenReturn(coefficient);
        return firePremiumService;
    }

    private static void addTheftPremiumService(PremiumServiceFactory premiumServiceFactory,
                                               BigDecimal riskSum, BigDecimal coefficient)
        throws PremiumCoefficientException, PremiumServiceNotDefinedException
    {
        TheftPremiumService theftPremiumService = mockTheftPremiumService(riskSum, coefficient);
        when(premiumServiceFactory.get(RiskType.THEFT)).thenReturn(theftPremiumService);
    }

    private static TheftPremiumService mockTheftPremiumService(BigDecimal riskSum, BigDecimal coefficient)
        throws PremiumRuleNotFoundException, MultiplePremiumRulesFoundException
    {
        TheftPremiumService theftPremiumService = mock(TheftPremiumService.class);
        when(theftPremiumService.getCoefficient(riskSum)).thenReturn(coefficient);
        return theftPremiumService;
    }

    /**
     * expectedPremium = firePremium + theftPremium = n * riskSum * fireCoef + n * riskSum * theftCoef
     * 7325.93 (7325.92674) = 2375.97624 + 4949.9505 = 99 * 999.99 * 0.024 + 99 * 999.99 * 0.05
     */
    private Arguments getArgumentsForLargeAmountPolicyObjects() throws PolicyCalculationException
    {
        int largeAmount = 99;
        double riskSum = 999.99;
        var mockedPolicyValidator = Mockito.mock(PolicyValidator.class);
        return Arguments.of(
            this.testPolicyService.getPolicyWithMultipleObjectsAndAllRiskTypes(largeAmount, riskSum),
            7325.93,
            mockPremiumServiceFactoryForLargeAmountPolicyObjects(),
            mockPolicyServiceForFireAndTheftPolicySubObjects(largeAmount, riskSum),
            mockedPolicyValidator
        );
    }

    private PolicyService mockPolicyServiceForFireAndTheftPolicySubObjects(int amount, double riskSum)
    {

        PolicyService policyService = mock(PolicyService.class);

        PolicySubObject fireSubObject = new PolicySubObjectBuilder(RiskType.FIRE).withSum(riskSum).make();
        PolicySubObject theftSubObject = new PolicySubObjectBuilder(RiskType.THEFT).withSum(riskSum).make();
        var subObjectsByRiskType = new HashMap<RiskType, List<PolicySubObject>>();
        subObjectsByRiskType.put(RiskType.FIRE, Collections.nCopies(amount, fireSubObject));
        subObjectsByRiskType.put(RiskType.THEFT, Collections.nCopies(amount, theftSubObject));

        when(policyService.getSubObjectsByRiskType(any())).thenReturn(subObjectsByRiskType);

        return policyService;
    }

    private PremiumServiceFactory mockPremiumServiceFactoryForLargeAmountPolicyObjects()
        throws PolicyCalculationException
    {
        PremiumServiceFactory premiumServiceFactory = mock(PremiumServiceFactory.class);
        // 98999.01 = 99 * 999.99
        addFirePremiumService(premiumServiceFactory, BigDecimal.valueOf(98999.01), BigDecimal.valueOf(0.024));
        addTheftPremiumService(premiumServiceFactory, BigDecimal.valueOf(98999.01), BigDecimal.valueOf(0.05));
        return premiumServiceFactory;
    }
}
