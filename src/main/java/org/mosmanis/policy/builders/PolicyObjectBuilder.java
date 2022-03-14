package org.mosmanis.policy.builders;

import org.mosmanis.policy.dto.PolicyObject;
import org.mosmanis.policy.dto.PolicySubObject;

import java.util.ArrayList;
import java.util.Collection;

public class PolicyObjectBuilder {
    private final Collection<PolicySubObject> policySubObjects;
    private String name;

    public PolicyObjectBuilder()
    {
        this("");
    }

    public PolicyObjectBuilder(String name)
    {
        this.name = name;
        this.policySubObjects = new ArrayList<>();
    }

    public PolicyObjectBuilder withSubObject(PolicySubObject policySubObject)
    {
        this.policySubObjects.add(policySubObject);
        return this;
    }

    public PolicyObjectBuilder withSubObjects(Collection<PolicySubObject> policySubObjects)
    {
        this.policySubObjects.addAll(policySubObjects);
        return this;
    }

    public PolicyObject make()
    {
        return new PolicyObject(this.name, this.policySubObjects);
    }
}
