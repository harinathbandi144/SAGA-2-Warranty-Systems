package com.saga2.dealer_service.kafka.event;

import com.saga2.dealer_service.enums.DealerStatus;

import java.time.Instant;

public record DealerStatusChangedEvent(
        String eventId,
        String dealerId,
        String dealerEmail,
        String oldStatus,
        String newStatus,
        Instant occurredAt
) {
}
