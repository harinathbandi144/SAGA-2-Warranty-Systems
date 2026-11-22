package com.saga2.dealer_service.kafka.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealerEventProducer {

    private final KafkaTemplate<String, DealerStatusChangedEvent> kafkaTemplate;

    public void publish(DealerStatusChangedEvent event) {

        kafkaTemplate.send(
                "dealer-status-events",
                event.dealerId(),
                event
        ).whenComplete((result, exception) -> {

            if (exception != null) {
                log.error(
                        "Failed to publish dealer status event for {}",
                        event.dealerId(),
                        exception
                );
            } else {
                log.info(
                        "Dealer status event published: dealerId={}, partition={}, offset={}",
                        event.dealerId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset()
                );
            }
        });
    }
}

