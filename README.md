# Policy Calculator

Technical Java Spring Assignment

Tech stack used:

* Java 17
* Spring Boot 3
* jUnit 5
* Maven

Initial project was created using Spring Initializr. I added a Spring Web dependency, which was not needed for this assignment. However, with it the project could be expanded with a Spring Controller to consume a policy and return its premium over REST API.
[See the configuration that was used.](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.0.0-SNAPSHOT&packaging=war&jvmVersion=17&groupId=org.mosmanis&artifactId=policy&name=policy&description=Policy%20Premium%20assignment&packageName=org.mosmanis.policy&dependencies=web)

## Core

###### Premium Calculator

`org.mosmanis.policy.calculators.PremiumCalculator`
To prevent possible issues with `double` type values during calculation, the class and its services operate only with the `BigDecimal` type.`double` is used only to store insured sum for Policy SubObjects and to return the total premium.

`PremiumCalculator#calculate(Policy policy): double`

What does it do:

1. Validate whether Policy is how we expect it to be (PolicyValidator)
2. Group all SubObjects by their risk type (PolicyService)
3. For each found risk type:  
   3.1 Calculate total insured sum of all SubObjects  
   3.2 Get the applicable risk type premium coefficient [(see Premium Rules)](#premium-rules)  
   3.3 Calculate the risk premium - multiply coefficient with total risk sum  
   3.4 Finally, add the current risk premium to the total premium
4. Set the decimal point for `BigDecimal` and return a `double` value

###### Premium Rules

Premium rules dictate which premium coefficient should be applied for a Policy insured risk sum.  
There are 3 main parts for Premium rules:

1. `org.mosmanis.policy.rules.premium.PremiumRule`  
   Responsibilities for each PremiumRule:

* Check if it applies to the given sum. (`doesApply(BigDecimal riskSum)`)
* Coefficient which to return if it does. (`getCoefficient(): BigDecimal`)

2. `org.mosmanis.policy.services.PremiumService`  
   Responsibilities for each PremiumService:

* Store a RiskType which this service is responsible for.
* Store rules that will be used for this RiskType.
* Find the applicable rule and return its coefficient. Throw exception if none or multiple rules are found. (`getCoefficient(BigDecimal riskSum): BigDecimal`)

3. `org.mosmanis.policy.factories.PremiumServiceFactory`  
   Responsibilities for PremiumServiceFactory:

* Store PremiumServices mapped to their RiskType.
* Get the assigned PremiumService or throw an exception.(`get(RiskType riskType): PremiumService`)

###### Adding a new Risk Type

1. Declare the new Risk Type in `org.mosmanis.policy.enums.RiskType`
2. Declare the new coefficients in  `org.mosmanis.policy.enums.PremiumCoefficients`  
   Example:

```
package org.mosmanis.policy.enums.premiumcoefficients;

public enum Fire {
    SUM_100_OR_LESS(0.014),
    SUM_MORE_THAN_100(0.024);

    public final double coefficient;

    Fire(double coefficient)
    {
        this.coefficient = coefficient;
    }
}
```

3. Create a new `org.mosmanis.policy.rules.premium.PremiumRule` for each coefficient.     
   Example:

```
package org.mosmanis.policy.rules.premium;

import org.mosmanis.policy.enums.premiumcoefficients.Theft;

import java.math.BigDecimal;

public class SumFifteenOrMore extends PremiumRule {
    public SumFifteenOrMore()
    {
        super(Theft.SUM_15_OR_MORE.coefficient);
    }

    @Override
    public boolean doesApply(BigDecimal riskSum)
    {
        return riskSum.compareTo(BigDecimal.valueOf(15)) >= 0;
    }
}
```

4. Create a new `org.mosmanis.policy.services.PremiumService` for the new RiskType.  
   Example:

```
public class FirePremiumService extends PremiumService {
    public FirePremiumService(@NonNull Collection<PremiumRule> premiumRules)
    {
        super(premiumRules, RiskType.FIRE);
    }
}
```

5. Declare the new PremiumService as a bean in `org.mosmanis.policy.AppConfig` and add it to the map bean `riskTypesToPremiumServices`  
   Example:

```
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
 ```

The Map will be autowired to PremiumServiceFactory by Spring Boot with annotations, so we are done.

###### Tests

All tests are located under `org.mosmanis.policy.tests`.  
Although not in requirements, there is integration test to check whether beans are created and wired correctly.  
Unit tests cover `PremiumCalculator`, `PolicyService,` `PolicyValidator`, each `PremiumService` and each `PremiumRule`. Test data is mostly provided using `ArgumentsProvider` interface, to divide test logic and data. Example:

```
 @ParameterizedTest
 @ArgumentsSource(PremiumCalculatorCalculateValues.class)
 void testCalculate(Policy policy, double expectedPremium, PremiumServiceFactory mockedPremiumServiceFactory,
                    PolicyService mockedPolicyService, PolicyValidator mockedPolicyValidator) throws Exception
 {
     var premiumCalculator =
         new PremiumCalculator(mockedPremiumServiceFactory, mockedPolicyService, mockedPolicyValidator);
     double actualPremium = premiumCalculator.calculate(policy);
     assertEquals(expectedPremium, actualPremium);
 }
```

And ArgumentsProvider:

```
public class PremiumCalculatorCalculateValues implements ArgumentsProvider {
    private final TestPolicyService testPolicyService;

    public PremiumCalculatorCalculateValues()
    {
        this.testPolicyService = new TestPolicyService();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            Arguments.of(this.testPolicyService.getEmptyPolicy(), 0,
                mockPremiumServiceFactoryForEmptyPolicy(),
                mockPolicyServiceForEmptyPolicy(),
                Mockito.mock(PolicyValidator.class)),
            Arguments.of(this.testPolicyService.getFirstAcceptanceCriteriaPolicy(), 2.28,
                mockPremiumServiceFactoryForFirstAcceptanceCriteria(),
                mockPolicyServiceForFirstAcceptanceCriteria(),
                Mockito.mock(PolicyValidator.class)),
            Arguments.of(this.testPolicyService.getSecondAcceptanceCriteriaPolicy(), 17.13,
                mockPremiumServiceFactoryForSecondAcceptanceCriteria(),
                mockPolicyServiceForSecondAcceptanceCriteria(),
                Mockito.mock(PolicyValidator.class)),
            getArgumentsForLargeAmountPolicyObjects()
        );
    }
    
    ...
```

For `PremiumCalculator#calculate`, the specified acceptance criteria is used as test cases, as well as other test data.

## Additional architecture

###### Policy builder

For Policy DTOs, Java `Record` class type is used.  
To help with creating test data, I created `PolicyBuilder`, `PolicyObjectBuilder` and `PolicySubObjectBuilder` classes. Example, creating the first acceptance criteria Policy:

```
return new PolicyBuilder().withPolicyObject(
   new PolicyObjectBuilder().withSubObjects(List.of(
       new PolicySubObjectBuilder(RiskType.FIRE).withSum(100.0).make(),
       new PolicySubObjectBuilder(RiskType.THEFT).withSum(8.0).make()
   )).make()
).make();
```

###### Test Policy Service

Service that provides methods for creating test data policies.  
Example, `ArgumentsProvider` that is using `TestPolicyService` for policy creation:

```
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
    {
        return Stream.of(
            Arguments.of(this.testPolicyService.getEmptyPolicy(), 0),
            Arguments.of(this.testPolicyService.getFirstAcceptanceCriteriaPolicy(), 2.28),
            Arguments.of(this.testPolicyService.getSecondAcceptanceCriteriaPolicy(), 17.13),
        );
    }
```

###### Policy validator

Initially some validation logic was in the Policy Builders, but later was moved to a separate class, so all validation rules are in one place.  
Validation rules, according to requirements:

* Policy is not null
* There is at least one Policy Object
* There can be no SubObjects for a Policy Object
* A SubObject insured sum cannot be negative.

Policy number is not validated.
