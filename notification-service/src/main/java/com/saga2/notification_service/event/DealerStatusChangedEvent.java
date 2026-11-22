package com.saga2.notification_service.event;

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
