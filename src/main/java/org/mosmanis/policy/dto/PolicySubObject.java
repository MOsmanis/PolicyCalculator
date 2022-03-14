package org.mosmanis.policy.dto;

import org.mosmanis.policy.enums.RiskType;

public record PolicySubObject(String name, double sumInsured, RiskType riskType) {
}
