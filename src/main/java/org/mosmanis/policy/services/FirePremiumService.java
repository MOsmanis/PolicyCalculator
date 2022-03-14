package org.mosmanis.policy.services;

import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.rules.premium.PremiumRule;
import org.springframework.lang.NonNull;

import java.util.Collection;

public class FirePremiumService extends PremiumService {
    public FirePremiumService(@NonNull Collection<PremiumRule> premiumRules)
    {
        super(premiumRules, RiskType.FIRE);
    }
}
