package com.aegis.fraud.service;

import com.aegis.common.event.TransactionEvent;
import com.aegis.common.event.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class FraudDetectionService {

    private static final BigDecimal FRAUD_THRESHOLD = new BigDecimal(10000.00);
    private static final String ALERT_TOPIC = "tx-fraud-alerts";

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public FraudDetectionService(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "tx-inbound", groupId = "fraud-detection-group")
    public void consumeTransaction(TransactionEvent event) {

        log.info("Received transaction to analyze: ID={}, Amount={}", event.transactionId(), event.amount());

        if(event.amount().compareTo(FRAUD_THRESHOLD) > 0) {

            log.warn(" ----- FRAUD ALERT ----- \n Publishing alert for Transaction {}", event.transactionId());

            TransactionEvent alertEvent = new TransactionEvent(
                event.transactionId(),
                event.userId(),
                event.amount(),
                event.currency(),
                event.timestamp(),
                TransactionStatus.FRAUD_ALERT
            );
            kafkaTemplate.send(ALERT_TOPIC, event.transactionId(), alertEvent);
        }
        else {
            log.info("Transaction {} is clean.", event.transactionId());
        }
    }
}
