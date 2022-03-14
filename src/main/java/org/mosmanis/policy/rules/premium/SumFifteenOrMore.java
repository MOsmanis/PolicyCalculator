package org.mosmanis.policy.rules.premium;

import org.mosmanis.policy.enums.PremiumCoefficient;

import java.math.BigDecimal;

public class SumFifteenOrMore extends PremiumRule {
    public SumFifteenOrMore()
    {
        super(PremiumCoefficient.SUM_15_OR_MORE.coefficient);
    }

    @Override
    public boolean doesApply(BigDecimal riskSum)
    {
        return riskSum.compareTo(BigDecimal.valueOf(15)) >= 0;
    }
}
