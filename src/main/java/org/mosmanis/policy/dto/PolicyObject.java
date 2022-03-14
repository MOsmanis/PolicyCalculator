package org.mosmanis.policy.dto;

import java.util.Collection;

public record PolicyObject(String name, Collection<PolicySubObject> subObjects) {
}
