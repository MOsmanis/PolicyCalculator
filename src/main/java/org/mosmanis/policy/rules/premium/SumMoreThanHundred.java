package org.mosmanis.policy.rules.premium;

import org.mosmanis.policy.enums.PremiumCoefficient;

import java.math.BigDecimal;

public class SumMoreThanHundred extends PremiumRule {
    public SumMoreThanHundred()
    {
        super(PremiumCoefficient.SUM_MORE_THAN_100.coefficient);
    }

    @Override
    public boolean doesApply(BigDecimal riskSum)
    {
        return riskSum.compareTo(BigDecimal.valueOf(100)) > 0;
    }
}
