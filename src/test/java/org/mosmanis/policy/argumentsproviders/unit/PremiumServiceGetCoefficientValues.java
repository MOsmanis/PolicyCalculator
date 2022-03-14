package org.mosmanis.policy.argumentsproviders.unit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mockito.Mockito;
import org.mosmanis.policy.rules.premium.PremiumRule;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public class PremiumServiceGetCoefficientValues implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        return Stream.of(getArguments());
    }

    private Arguments getArguments()
    {
        BigDecimal riskSum = BigDecimal.TEN;
        BigDecimal expectedCoefficient = BigDecimal.valueOf(0.014);

        Collection<PremiumRule> riskTypeToPremiumRules = getFireTypeToMockPremiumRules(riskSum, expectedCoefficient);

        return Arguments.of(riskTypeToPremiumRules, riskSum, expectedCoefficient);
    }

    private Collection<PremiumRule> getFireTypeToMockPremiumRules(BigDecimal riskSum, BigDecimal expectedCoefficient)
    {
        PremiumRule mockedFirePremiumRule = Mockito.mock(PremiumRule.class);
        when(mockedFirePremiumRule.doesApply(riskSum)).thenReturn(true);
        when(mockedFirePremiumRule.getCoefficient()).thenReturn(expectedCoefficient);

        PremiumRule mockedNonApplicableRule = Mockito.mock(PremiumRule.class);
        when(mockedNonApplicableRule.doesApply(riskSum)).thenReturn(false);

        return List.of(mockedFirePremiumRule, mockedNonApplicableRule, mockedNonApplicableRule);
    }
}
