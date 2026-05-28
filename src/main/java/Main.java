import br.com.zenon.models.FraudDemark;
import br.com.zenon.models.Origin;
import br.com.zenon.models.Transaction;
import br.com.zenon.models.types.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

void main() {
    List<Transaction> transactions = new ArrayList<>();

    transactions.add(new Transaction(
            1,
            TransactionType.PAYMENT,
            new BigDecimal("9839.64"),
            new Origin("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
            new Origin("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
            new FraudDemark(false, false)
    ));

    transactions.add(new Transaction(
            743,
            TransactionType.CASH_OUT,
            new BigDecimal("850002.52"),
            new Origin("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
            new Origin("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
            new FraudDemark(true, false)
    ));

    for (Transaction transaction : transactions) {
        IO.println(transaction);
    }
}
