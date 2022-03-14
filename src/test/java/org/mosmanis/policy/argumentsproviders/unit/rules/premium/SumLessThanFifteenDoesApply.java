package org.mosmanis.policy.argumentsproviders.unit.rules.premium;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class SumLessThanFifteenDoesApply implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
    {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(-10.0), false),
            Arguments.of(BigDecimal.ZERO, true),
            Arguments.of(BigDecimal.TEN, true),
            Arguments.of(BigDecimal.valueOf(14.99), true),
            Arguments.of(BigDecimal.valueOf(15.0), false),
            Arguments.of(BigDecimal.valueOf(101.0), false)
        );
    }
}
