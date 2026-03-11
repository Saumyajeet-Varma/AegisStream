package com.aegis.notification.service;

import com.aegis.common.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService {

    @KafkaListener(topics = "tx-fraud-alerts", groupId = "notification-group")
    public void handleFraudAlert(TransactionEvent event) {
        log.error("=====================================================");
        log.error("📧 SENDING URGENT EMAIL TO USER: {}", event.userId());
        log.error("Message: Your transaction of ${} has been blocked due to suspected fraud.", event.amount());
        log.error("Transaction ID: {}", event.transactionId());
        log.error("=====================================================");

        // TODO:In a real project, you would use JavaMailSender or AWS SES here to send a real email.
    }
}
