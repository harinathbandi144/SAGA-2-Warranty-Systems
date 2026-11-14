package com.saga.claim_service.dto;

import com.saga.claim_service.enums.Status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ClaimResponse(
        Long id,
        String claimNumber,
        String dealerId,
        String vin,
        String failureDescription,
        BigDecimal claimedAmount,
        Status status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
