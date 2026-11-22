package com.saga2.notification_service.event;

import com.saga2.notification_service.service.DealerStatusEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DealerStatusEventConsumer {

    private final DealerStatusEmailService emailService;

    @KafkaListener(
            topics = "dealer-status-events",
            groupId = "dealer-notification-group",
            containerFactory = "dealerKafkaListenerFactory"
    )
    public void consume(DealerStatusChangedEvent event) {

        log.info("Dealer status changed: {} -> {}",
                event.oldStatus(), event.newStatus());

        emailService.sendDealerStatusEmail(event);
    }
}
