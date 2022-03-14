package org.mosmanis.policy.calculators;

import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.exceptions.PolicyCalculationException;

public interface PolicyCalculator {
    double calculate(Policy policy) throws PolicyCalculationException;
}
