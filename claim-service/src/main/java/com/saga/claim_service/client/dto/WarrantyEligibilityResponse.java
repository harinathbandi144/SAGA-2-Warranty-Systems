package com.saga.claim_service.client.dto;

public record WarrantyEligibilityResponse(
        String vin,
        boolean eligible,
        String reason
) {
}
