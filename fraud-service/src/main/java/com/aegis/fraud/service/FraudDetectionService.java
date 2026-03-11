package com.aegis.fraud.service;

import com.aegis.common.event.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FraudDetectionService {

    private static final Logger log = LoggerFactory.getLogger(FraudDetectionService.class);

    private static final BigDecimal FRAUD_THRESHOLD = new BigDecimal(10000.00);

    @KafkaListener(topics = "tx-inbound", groupId = "fraud-detection-group")
    public void consumeTransaction(TransactionEvent event) {

        log.info("Received transaction to analyze: ID={}, Amount={}", event.transactionId(), event.amount());

        if(event.amount().compareTo(FRAUD_THRESHOLD) > 0) {
            log.warn(" ----- FRAUD ALERT ----- \n Transaction {} exceeds threshold! User: {}", event.transactionId(), event.userId());
        }
        else {
            log.info("Transaction {} is clean.", event.transactionId());
        }
    }
}
