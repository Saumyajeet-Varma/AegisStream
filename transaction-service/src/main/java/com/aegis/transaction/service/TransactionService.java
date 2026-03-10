package com.aegis.transaction.service;

import com.aegis.common.event.TransactionEvent;
import com.aegis.common.event.TransactionStatus;
import com.aegis.transaction.dto.TransactionRequest;
import com.aegis.transaction.model.Transaction;
import com.aegis.transaction.repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private static final String TOPIC = "tx-inbound";

    private final TransactionRepository repository;
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionService(TransactionRepository repository, KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Transaction processTransaction(TransactionRequest request) {

        // Create and Save to Database
        Transaction tx = new Transaction();

        tx.setUserId(request.userId());
        tx.setAmount(request.amount());
        tx.setCurrency(request.currency());
        tx.setStatus(TransactionStatus.PENDING);

        Transaction savedTx = repository.save(tx);

        // Create the Event Payload
        TransactionEvent event = new TransactionEvent(
                savedTx.getId(),
                savedTx.getUserId(),
                savedTx.getAmount(),
                savedTx.getCurrency(),
                savedTx.getTimestamp(),
                savedTx.getStatus()
        );

        // Publish to Kafka
        kafkaTemplate.send(TOPIC, savedTx.getId(), event);

        return savedTx;
    }
}
