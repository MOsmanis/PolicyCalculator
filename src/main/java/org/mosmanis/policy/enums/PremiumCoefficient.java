package org.mosmanis.policy.enums;

public enum PremiumCoefficient {
    SUM_LESS_THAN_15(0.11),
    SUM_15_OR_MORE(0.05),
    SUM_100_OR_LESS(0.014),
    SUM_MORE_THAN_100(0.024);

    public final double coefficient;

    PremiumCoefficient(double coefficient)
    {
        this.coefficient = coefficient;
    }
}
