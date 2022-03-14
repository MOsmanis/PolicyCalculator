package org.mosmanis.policy.services;

import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.exceptions.MultiplePremiumRulesFoundException;
import org.mosmanis.policy.exceptions.PremiumRuleNotFoundException;
import org.mosmanis.policy.rules.premium.PremiumRule;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public abstract class PremiumService {
    private final Collection<? extends PremiumRule> premiumRules;
    private final RiskType riskType;

    public PremiumService(Collection<? extends PremiumRule> premiumRules, RiskType riskType)
    {
        this.premiumRules = premiumRules;
        this.riskType = riskType;
    }

    public BigDecimal getCoefficient(BigDecimal riskSum)
        throws PremiumRuleNotFoundException, MultiplePremiumRulesFoundException
    {
        if (CollectionUtils.isEmpty(premiumRules)) {
            throw new PremiumRuleNotFoundException(getNoPremiumRulesDefinedMessage(riskType));
        }

        List<? extends PremiumRule> applicablePremiumRules =
            premiumRules.stream().filter(pr -> pr.doesApply(riskSum)).toList();

        if (applicablePremiumRules.isEmpty()) {
            throw new PremiumRuleNotFoundException(getRuleNotFoundMessage(riskType, riskSum));
        } else if (applicablePremiumRules.size() > 1) {
            throw new MultiplePremiumRulesFoundException(getMultipleRulesFoundMessage(riskType, riskSum));
        }

        return applicablePremiumRules.get(0).getCoefficient();
    }

    public Collection<? extends PremiumRule> getPremiumRules()
    {
        return premiumRules;
    }

    public RiskType getRiskType()
    {
        return riskType;
    }

    private String getMultipleRulesFoundMessage(RiskType riskType, BigDecimal riskSum)
    {
        return String.format("Found multiple premium coefficients for the given risk type (%s) and sum (%f)",
            riskType.toString(), riskSum.doubleValue());
    }

    private String getRuleNotFoundMessage(RiskType riskType, BigDecimal riskSum)
    {
        return String.format("Could not find a premium coefficient for the given risk type (%s) and sum (%f)",
            riskType.toString(), riskSum.doubleValue());
    }

    private String getNoPremiumRulesDefinedMessage(RiskType riskType)
    {
        return String.format("No defined premium coefficients for the given risk type (%s)", riskType.toString());
    }
}
