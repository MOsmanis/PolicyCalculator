package org.mosmanis.policy.argumentsproviders.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mosmanis.policy.builders.PolicyBuilder;
import org.mosmanis.policy.builders.PolicyObjectBuilder;
import org.mosmanis.policy.builders.PolicySubObjectBuilder;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.enums.PolicyStatus;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.exceptions.PolicyValidationException;
import org.mosmanis.policy.services.TestPolicyService;

import java.util.stream.Stream;

public class PolicyValidatorExceptions implements ArgumentsProvider {
    TestPolicyService testPolicyService;

    public PolicyValidatorExceptions()
    {
        this.testPolicyService = new TestPolicyService();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            Arguments.of(null, PolicyValidationException.class),
            Arguments.of(new Policy("", PolicyStatus.REGISTERED, null), PolicyValidationException.class),
            getArgumentsForNegativeSubObjectsum()
        );
    }

    private Arguments getArgumentsForNegativeSubObjectsum()
    {
        Policy policy = new PolicyBuilder().withPolicyObject(
            new PolicyObjectBuilder().withSubObject(
                new PolicySubObjectBuilder(RiskType.FIRE).withSum(-99).make()
            ).make()
        ).make();
        return Arguments.of(policy, PolicyValidationException.class);
    }
}
