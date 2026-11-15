package com.saga2.vehicle_service.dto;

public record WarrantyEligibilityResponse(
        String vin,
        boolean eligible,
        String reason
)
{}
