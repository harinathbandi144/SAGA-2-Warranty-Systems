package com.saga2.notification_service.event;

public record ClaimSubmittedEvent(
        Long claimId,
        String vin,
        String status
) {}
