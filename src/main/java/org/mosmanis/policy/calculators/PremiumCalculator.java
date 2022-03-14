package org.mosmanis.policy.calculators;

import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;
import org.mosmanis.policy.exceptions.PolicyCalculationException;
import org.mosmanis.policy.factories.PremiumServiceFactory;
import org.mosmanis.policy.services.PolicyService;
import org.mosmanis.policy.validators.PolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class PremiumCalculator implements PolicyCalculator {
    private final PremiumServiceFactory premiumServiceFactory;
    private final PolicyService policyService;
    private final PolicyValidator policyValidator;

    @Autowired
    public PremiumCalculator(PremiumServiceFactory premiumServiceFactory, PolicyService policyService,
                             PolicyValidator policyValidator)
    {
        this.premiumServiceFactory = premiumServiceFactory;
        this.policyService = policyService;
        this.policyValidator = policyValidator;
    }

    @Override
    public double calculate(Policy policy) throws PolicyCalculationException
    {
        policyValidator.validate(policy);

        Map<RiskType, List<PolicySubObject>> riskTypesToSubObjects = policyService.getSubObjectsByRiskType(policy);

        BigDecimal totalPremium = BigDecimal.ZERO;
        for (var riskTypeToSubObjects : riskTypesToSubObjects.entrySet()) {
            totalPremium = totalPremium.add(getRiskPremium(riskTypeToSubObjects));
        }
        totalPremium = totalPremium.setScale(2, RoundingMode.HALF_UP);

        return totalPremium.doubleValue();
    }

    private BigDecimal getRiskPremium(Map.Entry<RiskType, List<PolicySubObject>> riskTypeToSubObjects)
        throws PolicyCalculationException
    {
        BigDecimal riskSum = getTotalSum(riskTypeToSubObjects.getValue());
        BigDecimal premiumCoefficient =
            premiumServiceFactory.get(riskTypeToSubObjects.getKey()).getCoefficient(riskSum);

        return riskSum.multiply(premiumCoefficient);
    }

    private BigDecimal getTotalSum(List<PolicySubObject> policySubObjects)
    {
        return policySubObjects.stream().map(pso -> BigDecimal.valueOf(pso.sumInsured()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
