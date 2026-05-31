package br.com.zenon.models;

import br.com.zenon.constants.ErrorMessages;

import java.math.BigDecimal;
import java.util.Objects;

public record Origin(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {
    public Origin {
        Objects.requireNonNull(name);
        Objects.requireNonNull(oldBalance);
        Objects.requireNonNull(newBalance);
        validateOrigin(oldBalance, newBalance, name);
    }

    private void validateOrigin(BigDecimal oldBalance, BigDecimal newBalance, String name) {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_EMPTY_NAME_PROP);
        }

        if (oldBalance != null && oldBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_BIGDECIMAL_PROP);
        }

        if (newBalance != null && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_BIGDECIMAL_PROP);
        }
    }

}
