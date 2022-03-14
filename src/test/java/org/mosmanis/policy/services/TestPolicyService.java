package org.mosmanis.policy.services;

import org.mosmanis.policy.builders.PolicyBuilder;
import org.mosmanis.policy.builders.PolicyObjectBuilder;
import org.mosmanis.policy.builders.PolicySubObjectBuilder;
import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicyObject;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates policies for testing purposes
 */
public class TestPolicyService {
    public Policy getEmptyPolicy()
    {
        return new PolicyBuilder().withPolicyObject(new PolicyObjectBuilder().make()).make();
    }

    public Policy getEmptyPolicyWithNullSubObjects()
    {
        return new PolicyBuilder().withPolicyObject(new PolicyObject("", null)).make();
    }

    /**
     * First acceptance criteria as defined in the requirements:
     * If there is one policy, one object and two sub-objects as described below, then calculator should return
     * premium = 2.28 EUR
     * Risk type = FIRE, Sum insured = 100.00
     * Risk type = THEFT, Sum insured = 8.00
     */
    public Policy getFirstAcceptanceCriteriaPolicy()
    {
        return new PolicyBuilder().withPolicyObject(
            new PolicyObjectBuilder().withSubObjects(List.of(
                new PolicySubObjectBuilder(RiskType.FIRE).withSum(100.0).make(),
                new PolicySubObjectBuilder(RiskType.THEFT).withSum(8.0).make()
            )).make()
        ).make();
    }

    /**
     * First acceptance criteria as defined in the requirements:
     * If policy's total sum insured for risk type FIRE and total sum insured for risk type THEFT are as
     * described below, then calculator should return premium = 17.13 EUR
     * Risk type = FIRE, Sum insured = 500.00
     * Risk type = THEFT, Sum insured = 102.51
     */
    public Policy getSecondAcceptanceCriteriaPolicy()
    {
        return new PolicyBuilder().withPolicyObject(
            new PolicyObjectBuilder().withSubObjects(List.of(
                new PolicySubObjectBuilder(RiskType.FIRE).withSum(500.0).make(),
                new PolicySubObjectBuilder(RiskType.THEFT).withSum(102.51).make()
            )).make()
        ).make();
    }

    public Policy getPolicyWithTwoObjectsAndAllRiskTypes(double riskSum)
    {
        return getPolicyWithMultipleObjectsAndAllRiskTypes(2, riskSum);
    }

    public Policy getPolicyWithMultipleObjectsAndAllRiskTypes(int objectCount, double riskSum)
    {
        var allRiskTypeSubObjects = new ArrayList<PolicySubObject>();
        for (RiskType riskType : RiskType.values()) {
            allRiskTypeSubObjects.add(new PolicySubObjectBuilder(riskType).withSum(riskSum).make());
        }
        var multiplePolicyObjects = new ArrayList<PolicyObject>();
        PolicyObject policyObject = new PolicyObjectBuilder().withSubObjects(allRiskTypeSubObjects).make();
        for (int i = 0; i < objectCount; i++) {
            multiplePolicyObjects.add(policyObject);
        }
        return new PolicyBuilder().withPolicyObjects(multiplePolicyObjects).make();
    }
}
