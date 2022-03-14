package org.mosmanis.policy.argumentsproviders.unit.rules.premium;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class SumMoreThanHundredDoesApply implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(-10.0), false),
            Arguments.of(BigDecimal.valueOf(99.99), false),
            Arguments.of(BigDecimal.valueOf(100.0), false),
            Arguments.of(BigDecimal.valueOf(101.0), true),
            Arguments.of(BigDecimal.valueOf(500.0), true),
            Arguments.of(BigDecimal.valueOf(9999.99), true)
        );
    }
}
