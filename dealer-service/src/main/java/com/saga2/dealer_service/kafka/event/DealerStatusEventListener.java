package com.saga2.dealer_service.kafka.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class DealerStatusEventListener {

    private final DealerEventProducer dealerEventProducer;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(DealerStatusChangedEvent event) {
        dealerEventProducer.publish(event);
    }
}
