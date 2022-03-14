package org.mosmanis.policy.builders;

import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;

public class PolicySubObjectBuilder {
    private final RiskType riskType;
    private String name;
    private double sum;

    public PolicySubObjectBuilder(RiskType riskType)
    {
        this.name = "";
        this.sum = 0;
        this.riskType = riskType;
    }

    public PolicySubObjectBuilder withSum(double sum)
    {
        this.sum = sum;
        return this;
    }

    public PolicySubObject make()
    {
        return new PolicySubObject(this.name, this.sum, this.riskType);
    }
}
