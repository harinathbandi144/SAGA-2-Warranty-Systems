package com.saga2.notification_service.consumer;

import com.saga2.notification_service.event.ClaimSubmittedEvent;
import com.saga2.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClaimEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "claim-events",
            groupId = "notification-service",
            containerFactory = "claimKafkaListenerFactory"
    )
    public void consumer(ClaimSubmittedEvent claimSubmittedEvent){
        System.out.println("Notification received for Claim ID: "+claimSubmittedEvent.claimId());

        emailService.sendClaimConfirmation(claimSubmittedEvent);
    }
}
