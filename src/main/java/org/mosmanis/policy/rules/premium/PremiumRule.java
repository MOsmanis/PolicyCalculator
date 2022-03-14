package org.mosmanis.policy.rules.premium;

import java.math.BigDecimal;

public abstract class PremiumRule {
    private final BigDecimal coefficient;

    public PremiumRule(double coefficient)
    {
        this.coefficient = BigDecimal.valueOf(coefficient);
    }

    public abstract boolean doesApply(BigDecimal riskSum);

    public BigDecimal getCoefficient()
    {
        return coefficient;
    }
}
