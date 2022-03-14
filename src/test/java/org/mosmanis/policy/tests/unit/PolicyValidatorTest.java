package org.mosmanis.policy.tests.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.argumentsproviders.unit.PolicyValidatorExceptions;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.exceptions.PolicyValidationException;
import org.mosmanis.policy.validators.PolicyValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolicyValidatorTest {
    private static PolicyValidator policyValidator;

    @BeforeAll
    static void setUp() {
        policyValidator = new PolicyValidator();
    }

    @ParameterizedTest
    @ArgumentsSource(PolicyValidatorExceptions.class)
    void testValidate(Policy policy, Class<? extends PolicyValidationException> expectedException)
    {
        assertThrows(expectedException, () -> policyValidator.validate(policy));
    }
}
