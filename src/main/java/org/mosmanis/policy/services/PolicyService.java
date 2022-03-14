package org.mosmanis.policy.services;

import org.mosmanis.policy.dto.Policy;
import org.mosmanis.policy.dto.PolicyObject;
import org.mosmanis.policy.dto.PolicySubObject;
import org.mosmanis.policy.enums.RiskType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PolicyService {
    public Map<RiskType, List<PolicySubObject>> getSubObjectsByRiskType(@NonNull Policy policy)
    {
        return policy.objects().stream()
            .flatMap(this::getPolicySubObjectStream)
            .collect(Collectors.groupingBy(PolicySubObject::riskType));
    }

    private Stream<PolicySubObject> getPolicySubObjectStream(PolicyObject po)
    {
        return po.subObjects() != null ? po.subObjects().stream() : Stream.empty();
    }
}
