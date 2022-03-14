package org.mosmanis.policy;

import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.rules.premium.SumFifteenOrMore;
import org.mosmanis.policy.rules.premium.SumHundredOrLess;
import org.mosmanis.policy.rules.premium.SumLessThanFifteen;
import org.mosmanis.policy.rules.premium.SumMoreThanHundred;
import org.mosmanis.policy.services.FirePremiumService;
import org.mosmanis.policy.services.PremiumService;
import org.mosmanis.policy.services.TheftPremiumService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public Map<RiskType, ? extends PremiumService> riskTypesToPremiumServices()
    {
        var riskTypesToPremiumServices = new HashMap<RiskType, PremiumService>();
        riskTypesToPremiumServices.put(RiskType.FIRE, firePremiumService());
        riskTypesToPremiumServices.put(RiskType.THEFT, theftPremiumService());
        return riskTypesToPremiumServices;
    }

    @Bean
    public FirePremiumService firePremiumService()
    {
        return new FirePremiumService(List.of(new SumHundredOrLess(), new SumMoreThanHundred()));
    }

    @Bean
    public TheftPremiumService theftPremiumService()
    {
        return new TheftPremiumService(List.of(new SumLessThanFifteen(), new SumFifteenOrMore()));
    }
}
