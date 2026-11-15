package com.saga.claim_service.dto;

import com.saga.claim_service.enums.Status;

import java.time.OffsetDateTime;

public record ClaimHistoryResponse(
        Long id,
        Status oldStatus,
        Status newStatus,
        OffsetDateTime changedAt
) {
}
