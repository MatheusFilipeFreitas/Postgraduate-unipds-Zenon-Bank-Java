package br.com.zenon.io.implementation;

import br.com.zenon.constants.DefaultColumns;
import br.com.zenon.io.IMapper;
import br.com.zenon.models.ReportTransaction;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

public class ReportTransactionMapper implements IMapper<ReportTransaction> {
    private int amountIndex = 2;
    private int fraudIndex = 9;

    public ReportTransaction aggregate(Stream<String> lines) {
        var iterator = lines.iterator();
        if (!iterator.hasNext()) {
            return new ReportTransaction(0, 0, BigDecimal.ZERO);
        }

        initializeHeaders(iterator.next().split(","));

        long totalTransactions = 0;
        long totalFrauds = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        while (iterator.hasNext()) {
            String[] columns = iterator.next().split(",");
            totalTransactions++;
            totalFrauds += Long.parseLong(columns[fraudIndex]);
            totalAmount = totalAmount.add(new BigDecimal(columns[amountIndex]));
        }

        return new ReportTransaction(totalTransactions, totalFrauds, totalAmount);
    }

    @Override
    public ReportTransaction parse(String[] values) throws Exception {
        return aggregate(Arrays.stream(values));
    }

    @Override
    public void initializeHeaders(String[] headers) {
        amountIndex = findColumnIndex(headers, DefaultColumns.AMOUNT);
        fraudIndex = findColumnIndex(headers, DefaultColumns.FRAUD_INDICATOR);
    }

    private int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(columnName)) {
                return i;
            }
        }
        return -1;
    }
}
