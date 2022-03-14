package org.mosmanis.policy.builders;

import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicyObject;
import org.mosmanis.policy.enums.PolicyStatus;

import java.util.ArrayList;
import java.util.Collection;

public class PolicyBuilder {
    private final Collection<PolicyObject> policyObjects;
    private final String number;
    private PolicyStatus policyStatus;

    public PolicyBuilder()
    {
        this("");
    }

    public PolicyBuilder(String number)
    {
        this.number = number;
        this.policyStatus = PolicyStatus.REGISTERED;
        this.policyObjects = new ArrayList<>();
    }

    public PolicyBuilder withPolicyStatus(PolicyStatus policyStatus)
    {
        this.policyStatus = policyStatus;
        return this;
    }

    public PolicyBuilder withPolicyObject(PolicyObject policyObject)
    {
        this.policyObjects.add(policyObject);
        return this;
    }

    public PolicyBuilder withPolicyObjects(Collection<PolicyObject> policyObjects)
    {
        this.policyObjects.addAll(policyObjects);
        return this;
    }

    public Policy make()
    {
        return new Policy(this.number, this.policyStatus, this.policyObjects);
    }
}
