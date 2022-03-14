package org.mosmanis.policy.dto;

import org.mosmanis.policy.enums.PolicyStatus;

import java.util.Collection;

public record Policy(String number, PolicyStatus status, Collection<PolicyObject> objects) {
}
