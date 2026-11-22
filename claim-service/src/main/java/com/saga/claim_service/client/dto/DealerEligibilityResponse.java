package com.saga.claim_service.client.dto;

public record DealerEligibilityResponse(
        String dealerId,
        boolean eligible,
        String status
) {
}
