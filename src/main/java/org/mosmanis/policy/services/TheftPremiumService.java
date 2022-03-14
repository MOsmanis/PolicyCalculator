package org.mosmanis.policy.services;

import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.rules.premium.PremiumRule;
import org.springframework.lang.NonNull;

import java.util.Collection;

public class TheftPremiumService extends PremiumService {
    public TheftPremiumService(@NonNull Collection<PremiumRule> premiumRules)
    {
        super(premiumRules, RiskType.THEFT);
    }
}
