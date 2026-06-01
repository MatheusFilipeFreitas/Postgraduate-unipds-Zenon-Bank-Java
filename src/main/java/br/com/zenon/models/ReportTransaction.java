package br.com.zenon.models;

import java.math.BigDecimal;

public record ReportTransaction
(
    long totalTransactions,
    long totalFrauds,
    BigDecimal totalAmount
) {

}
