package org.mosmanis.policy.argumentsproviders.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.services.TestPolicyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class PolicyServiceValues implements ArgumentsProvider {
    private final TestPolicyService testPolicyService;

    public PolicyServiceValues()
    {
        this.testPolicyService = new TestPolicyService();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            Arguments.of(testPolicyService.getEmptyPolicy(), new HashMap<RiskType, Collection<PolicySubObject>>()),
            getArgumentsForMultipleObjectsAndAllRiskTypes(),
            Arguments.of(testPolicyService.getEmptyPolicyWithNullSubObjects(), new HashMap<RiskType, Collection<PolicySubObject>>())
        );
    }

    private Arguments getArgumentsForMultipleObjectsAndAllRiskTypes()
    {
        double riskSum = 0;
        var expectedRiskTypesToSubObjects = new HashMap<RiskType, Collection<PolicySubObject>>();

        PolicySubObject fireSubObject = new PolicySubObject("", riskSum, RiskType.FIRE);
        PolicySubObject theftSubObject = new PolicySubObject("", riskSum, RiskType.THEFT);

        expectedRiskTypesToSubObjects.put(fireSubObject.riskType(), List.of(fireSubObject, fireSubObject));
        expectedRiskTypesToSubObjects.put(theftSubObject.riskType(), List.of(theftSubObject, theftSubObject));

        return Arguments.of(testPolicyService.getPolicyWithTwoObjectsAndAllRiskTypes(riskSum),
            expectedRiskTypesToSubObjects);
    }
}
