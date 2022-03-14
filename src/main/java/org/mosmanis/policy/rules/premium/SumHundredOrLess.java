package org.mosmanis.policy.rules.premium;

import org.mosmanis.policy.enums.PremiumCoefficient;

import java.math.BigDecimal;

public class SumHundredOrLess extends PremiumRule {
    public SumHundredOrLess()
    {
        super(PremiumCoefficient.SUM_100_OR_LESS.coefficient);
    }

    @Override
    public boolean doesApply(BigDecimal riskSum)
    {
        boolean isZeroOrMore = riskSum.compareTo(BigDecimal.ZERO) >= 0;
        boolean isHundredOrLess = riskSum.compareTo(BigDecimal.valueOf(100)) <= 0;
        return isZeroOrMore && isHundredOrLess;
    }
}
