package org.mosmanis.policy.tests.integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mosmanis.policy.AppConfig;
import org.mosmanis.policy.argumentsproviders.integration.PremiumCalculatorCalculateValues;
import org.mosmanis.policy.calculators.PremiumCalculator;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.exceptions.PolicyCalculationException;
import org.mosmanis.policy.factories.PremiumServiceFactory;
import org.mosmanis.policy.services.PolicyService;
import org.mosmanis.policy.validators.PolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class, PremiumServiceFactory.class, PremiumCalculator.class,
    PolicyService.class, PolicyValidator.class})
@WebAppConfiguration
public class PremiumCalculatorTest {
    @Autowired
    private ApplicationContext applicationContext;

    @ParameterizedTest
    @ArgumentsSource(PremiumCalculatorCalculateValues.class)
    void testCalculate(Policy policy, double expectedPremium) throws PolicyCalculationException
    {
        PremiumCalculator premiumCalculator = applicationContext.getBean(PremiumCalculator.class);
        double actualPremium = premiumCalculator.calculate(policy);
        assertEquals(expectedPremium, actualPremium);
    }
}
