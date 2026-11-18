package com.saga2.notification_service.service;

import com.saga2.notification_service.event.ClaimSubmittedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.notification.test-recipient}")
    private String recipientEmail;

    public void sendClaimConfirmation(ClaimSubmittedEvent event){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(senderEmail);
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Warranty Claim Submitted Successfully");

        mailMessage.setText(
                "Dear Dealer,\n\n" +
                        "Your warranty claim has been submitted successfully.\n\n" +
                        "Claim ID: " + event.claimId() + "\n" +
                        "Vehicle VIN: " + event.vin() + "\n" +
                        "Status: " + event.status() + "\n\n" +
                        "Our warranty team will review your claim.\n\n" +
                        "Regards,\nSAGA2 Warranty Team"
        );

        javaMailSender.send(mailMessage);
    }

}
