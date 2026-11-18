package com.saga.claim_service.kafka.producer;

import com.saga.claim_service.kafka.event.ClaimSubmittedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.event.KafkaEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClaimEventProducer {

    private final KafkaTemplate<String, ClaimSubmittedEvent> kafkaTemplate;

    public void publish(ClaimSubmittedEvent claimSubmittedEvent){
        kafkaTemplate.send("claim-events",
                                 claimSubmittedEvent.claimId().toString(), claimSubmittedEvent);
    }
}
