package org.mosmanis.policy.tests.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.PolicyServiceValues;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.services.PolicyService;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolicyServiceTest {
    private static PolicyService policyService;

    @BeforeAll
    static void setUp()
    {
        policyService = new PolicyService();
    }

    @ParameterizedTest
    @ArgumentsSource(PolicyServiceValues.class)
    void testGetSubObjectsByRiskType(Policy policy,
                                     Map<RiskType, Collection<PolicySubObject>> expectedRiskTypesToSubObjects)
    {
        assertEquals(expectedRiskTypesToSubObjects, policyService.getSubObjectsByRiskType(policy));
    }
}
