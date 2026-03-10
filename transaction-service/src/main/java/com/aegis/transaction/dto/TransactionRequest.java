package com.aegis.transaction.dto;

import java.math.BigDecimal;

public record TransactionRequest(
        String userId,
        BigDecimal amount,
        String currency
) {}
