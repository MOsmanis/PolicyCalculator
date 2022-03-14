package org.mosmanis.policy.rules.premium;

import org.mosmanis.policy.enums.PremiumCoefficient;

import java.math.BigDecimal;

public class SumLessThanFifteen extends PremiumRule {
    public SumLessThanFifteen()
    {
        super(PremiumCoefficient.SUM_LESS_THAN_15.coefficient);
    }

    @Override
    public boolean doesApply(BigDecimal riskSum)
    {
        boolean isZeroOrMore = riskSum.compareTo(BigDecimal.ZERO) >= 0;
        boolean isLessThanFifteen = riskSum.compareTo(BigDecimal.valueOf(15)) < 0;
        return isZeroOrMore && isLessThanFifteen;
    }
}
