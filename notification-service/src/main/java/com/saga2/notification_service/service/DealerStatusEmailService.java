package com.saga2.notification_service.service;

import com.saga2.notification_service.event.DealerStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DealerStatusEmailService {

    private final JavaMailSender mailSender;

    public void sendDealerStatusEmail(DealerStatusChangedEvent event) {

        if (event.dealerEmail() == null ||
                event.dealerEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Dealer email is missing for: " + event.dealerId()
            );
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(event.dealerEmail());
        message.setSubject("SAGA2 - Dealer Account Status Updated");

        message.setText(
                "Dealer ID: " + event.dealerId() +
                        "\nPrevious Status: " + event.oldStatus() +
                        "\nNew Status: " + event.newStatus() +
                        "\nUpdated At: " + event.occurredAt()
        );

        mailSender.send(message);
    }
}
