package org.mosmanis.policy.validators;

import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicyObject;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.exceptions.PolicyValidationException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class PolicyValidator {
    public void validate(Policy policy) throws PolicyValidationException
    {
        if (policy == null) {
            throw new PolicyValidationException("Policy is null");
        } else if (policy.objects() == null || policy.objects().isEmpty()) {
            throw new PolicyValidationException("Policy has no objects");
        }
        for(PolicyObject policyObject : policy.objects()) {
            validatePolicyObject(policyObject);
        }
    }

    private void validatePolicyObject(PolicyObject policyObject) throws PolicyValidationException
    {
        Collection<PolicySubObject> policySubObjects = policyObject.subObjects();
        if(policySubObjects == null || policySubObjects.isEmpty()) {
            return;
        }
        for (PolicySubObject policySubObject : policySubObjects) {
            validatePolicySubObject(policySubObject);
        }
    }

    private void validatePolicySubObject(PolicySubObject policySubObject) throws PolicyValidationException
    {
        if (policySubObject.sumInsured() < 0) {
            throw new PolicyValidationException("One of Policy SubObjects has a negative sum insured");
        }
    }


}
