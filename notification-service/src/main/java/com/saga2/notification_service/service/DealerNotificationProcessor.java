package com.saga2.notification_service.service;

import com.saga2.notification_service.event.DealerStatusChangedEvent;
import com.saga2.notification_service.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealerNotificationProcessor {

    private final ProcessedEventRepository repository;
    private final DealerStatusEmailService emailService;

    public void process(DealerStatusChangedEvent event) {

        String eventId = event.eventId();

        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("Missing event ID");
        }

        boolean claimed = repository.claimEvent(eventId);

        if (!claimed) {

            String status = repository.findStatus(eventId);

            if ("SENT".equals(status)) {
                log.info("Duplicate event skipped: {}", eventId);
                return;
            }

            throw new IllegalStateException(
                    "Event not completed: " + eventId
            );
        }

        try {
            emailService.sendDealerStatusEmail(event);
        } catch (Exception ex) {
            repository.releaseFailedEvent(eventId);
            throw ex;
        }

        repository.markSent(eventId);

        log.info("Event processed successfully: {}", eventId);
    }
}
