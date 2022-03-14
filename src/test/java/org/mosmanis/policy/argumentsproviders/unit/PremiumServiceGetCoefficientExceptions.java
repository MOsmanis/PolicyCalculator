package org.mosmanis.policy.argumentsproviders.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mockito.Mockito;
import org.mosmanis.policy.exceptions.MultiplePremiumRulesFoundException;
import org.mosmanis.policy.exceptions.PremiumRuleNotFoundException;
import org.mosmanis.policy.rules.premium.PremiumRule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public class PremiumServiceGetCoefficientExceptions implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        return Stream.of(
            getArgumentsForNoPremiumRules(),
            getArgumentsForWrongPremiumRule(),
            getArgumentsForMultipleSamePremiumRules()
        );
    }

    private Arguments getArgumentsForNoPremiumRules()
    {
        return Arguments.of(new ArrayList<>(), BigDecimal.ZERO, PremiumRuleNotFoundException.class);
    }

    private Arguments getArgumentsForWrongPremiumRule()
    {
        BigDecimal riskSum = BigDecimal.ZERO;

        Collection<PremiumRule> premiumRules = getFireTypeToSingleMockPremiumRule(riskSum);

        return Arguments.of(premiumRules, riskSum, PremiumRuleNotFoundException.class);
    }

    private Arguments getArgumentsForMultipleSamePremiumRules()
    {
        BigDecimal riskSum = BigDecimal.ZERO;

        Collection<PremiumRule> riskTypeToPremiumRules = getFireTypeToMultipleSameMockPremiumRules(riskSum);

        return Arguments.of(riskTypeToPremiumRules, riskSum, MultiplePremiumRulesFoundException.class);
    }

    private Collection<PremiumRule> getFireTypeToSingleMockPremiumRule(BigDecimal riskSum)
    {
        PremiumRule mockedFirePremiumRule = Mockito.mock(PremiumRule.class);
        when(mockedFirePremiumRule.doesApply(riskSum)).thenReturn(false);

        return List.of(mockedFirePremiumRule);
    }

    private Collection<PremiumRule> getFireTypeToMultipleSameMockPremiumRules(BigDecimal riskSum)
    {
        PremiumRule mockedFirePremiumRule = Mockito.mock(PremiumRule.class);
        when(mockedFirePremiumRule.doesApply(riskSum)).thenReturn(true);
        PremiumRule mockedInapplicablePremiumRule = Mockito.mock(PremiumRule.class);
        when(mockedInapplicablePremiumRule.doesApply(riskSum)).thenReturn(false);

        return List.of(mockedFirePremiumRule, mockedFirePremiumRule, mockedInapplicablePremiumRule);
    }
}
