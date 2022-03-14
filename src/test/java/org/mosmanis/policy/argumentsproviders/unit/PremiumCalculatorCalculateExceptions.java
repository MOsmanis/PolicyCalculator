package org.mosmanis.policy.argumentsproviders.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mockito.Mockito;
import org.mosmanis.policy.builders.PolicyBuilder;
import org.mosmanis.policy.builders.PolicyObjectBuilder;
import org.mosmanis.policy.builders.PolicySubObjectBuilder;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.exceptions.*;
import org.mosmanis.policy.factories.PremiumServiceFactory;
import org.mosmanis.policy.services.PolicyService;
import org.mosmanis.policy.services.PremiumService;
import org.mosmanis.policy.services.TestPolicyService;
import org.mosmanis.policy.validators.PolicyValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

public class PremiumCalculatorCalculateExceptions implements ArgumentsProvider {
    private final TestPolicyService testPolicyService;

    public PremiumCalculatorCalculateExceptions()
    {
        this.testPolicyService = new TestPolicyService();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            getArgumentsForException(PremiumRuleNotFoundException.class),
            getArgumentsForException(MultiplePremiumRulesFoundException.class),
            getArgumentsForPremiumServiceNotDefinedException(),
            getArgumentsForPolicyValidationException()
        );
    }

    private Arguments getArgumentsForPolicyValidationException() throws PolicyValidationException
    {
        var expectedExceptionClass = PolicyValidationException.class;
        Policy policy = new PolicyBuilder().withPolicyObject(
            new PolicyObjectBuilder().withSubObject(
                new PolicySubObjectBuilder(RiskType.FIRE).make()
            ).make()).make();
        var mockedPolicyService = Mockito.mock(PolicyService.class);
        var mockedPremiumServiceFactory = Mockito.mock(PremiumServiceFactory.class);
        var mockedPolicyValidator = Mockito.mock(PolicyValidator.class);
        doThrow(expectedExceptionClass).when(mockedPolicyValidator).validate(policy);

        return Arguments.of(policy, mockedPremiumServiceFactory, mockedPolicyService, mockedPolicyValidator,
            expectedExceptionClass);
    }

    private Arguments getArgumentsForPremiumServiceNotDefinedException() throws PolicyCalculationException
    {
        var expectedExceptionClass = PremiumServiceNotDefinedException.class;
        PolicySubObject policySubObject = new PolicySubObjectBuilder(RiskType.FIRE).make();
        Policy policy = new PolicyBuilder().withPolicyObject(
            new PolicyObjectBuilder().withSubObject(policySubObject).make()).make();
        var mockedPolicyService = Mockito.mock(PolicyService.class);
        when(mockedPolicyService.getSubObjectsByRiskType(policy)).thenReturn(Map.of(RiskType.FIRE,
            List.of(policySubObject)));
        var mockedPremiumServiceFactory = Mockito.mock(PremiumServiceFactory.class);
        when(mockedPremiumServiceFactory.get(RiskType.FIRE)).thenThrow(expectedExceptionClass);
        var mockedPolicyValidator = Mockito.mock(PolicyValidator.class);

        return Arguments.of(policy, mockedPremiumServiceFactory, mockedPolicyService, mockedPolicyValidator,
            expectedExceptionClass);
    }

    private Arguments getArgumentsForException(Class<? extends PolicyCalculationException> expectedExceptionClass)
        throws PolicyCalculationException
    {
        double sumInsured = 0;
        PolicySubObject policySubObject = new PolicySubObjectBuilder(RiskType.FIRE).withSum(sumInsured).make();
        Policy policy = new PolicyBuilder().withPolicyObject(
            new PolicyObjectBuilder().withSubObject(policySubObject).make()).make();
        var mockedPolicyService = Mockito.mock(PolicyService.class);
        when(mockedPolicyService.getSubObjectsByRiskType(policy)).thenReturn(Map.of(RiskType.FIRE,
            List.of(policySubObject)));
        var mockedPremiumService = mock(PremiumService.class);
        when(mockedPremiumService.getCoefficient(BigDecimal.valueOf(sumInsured))).thenThrow(expectedExceptionClass);
        var mockedPremiumServiceFactory = Mockito.mock(PremiumServiceFactory.class);
        when(mockedPremiumServiceFactory.get(RiskType.FIRE)).thenReturn(mockedPremiumService);
        var mockedPolicyValidator = Mockito.mock(PolicyValidator.class);

        return Arguments.of(policy, mockedPremiumServiceFactory, mockedPolicyService, mockedPolicyValidator,
            expectedExceptionClass);
    }
}
