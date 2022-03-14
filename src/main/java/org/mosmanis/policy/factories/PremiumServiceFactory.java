package org.mosmanis.policy.factories;

import jakarta.annotation.Resource;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.exceptions.PremiumServiceNotDefinedException;
import org.mosmanis.policy.services.PremiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PremiumServiceFactory {
    @Resource
    private final Map<RiskType, ? extends PremiumService> riskTypesToPremiumServices;

    public PremiumServiceFactory(Map<RiskType, ? extends PremiumService> riskTypesToPremiumServices)
    {
        this.riskTypesToPremiumServices = riskTypesToPremiumServices;
    }

    public PremiumService get(RiskType riskType) throws PremiumServiceNotDefinedException
    {
        PremiumService premiumService = riskTypesToPremiumServices.get(riskType);
        if (premiumService == null) {
            throw new PremiumServiceNotDefinedException(getPremiumServiceNotFoundMessage(riskType));
        }
        return premiumService;
    }

    private String getPremiumServiceNotFoundMessage(RiskType riskType)
    {
        return String.format("No defined PremiumService for the given risk type (%s)", riskType.toString());
    }
}
