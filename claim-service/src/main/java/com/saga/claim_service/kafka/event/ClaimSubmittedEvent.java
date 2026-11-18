package com.saga.claim_service.kafka.event;

public record ClaimSubmittedEvent(
        Long claimId,
        String vin,
        String status
) {
}
