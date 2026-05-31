package br.com.zenon.models;

import br.com.zenon.constants.ErrorMessages;
import br.com.zenon.models.types.TransactionType;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(
        int step,
        TransactionType type,
        BigDecimal amount,
        Origin origin,
        Origin destinatary,
        FraudDemark fraudDemark
) {
    public Transaction {
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(destinatary);
        Objects.requireNonNull(fraudDemark);
        validateTransaction(step, amount);
    }

    private void validateTransaction(int step, BigDecimal amount) {
        if (step < 1) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_SIZE_PROP);
        }
        if (amount != null && amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_BIGDECIMAL_PROP);
        }
    }
}
