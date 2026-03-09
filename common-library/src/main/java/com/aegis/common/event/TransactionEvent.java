package com.aegis.common.event;

import java.math.BigDecimal;

public record TransactionEvent(
        String transactionId,
        String userId,
        BigDecimal amount,
        String currency,
        long timestamp,
        TransactionStatus status
) {}
