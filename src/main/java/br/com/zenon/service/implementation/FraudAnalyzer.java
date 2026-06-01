package br.com.zenon.service.implementation;

import br.com.zenon.models.Transaction;
import br.com.zenon.presentation.FraudDashboardPresenter;
import br.com.zenon.service.IAnalyzer;

import java.util.List;

public class FraudAnalyzer implements IAnalyzer<Transaction> {
    private final FraudDashboardPresenter presenter = new FraudDashboardPresenter();

    @Override
    public void analyze(List<Transaction> transactions) {
        presenter.display(getFraudsFromTransactions(transactions));
    }

    @Override
    public List<Transaction> getFraudsFromTransactions(List<Transaction> transactions) {
        return transactions
                .stream()
                .filter(transaction -> transaction.fraudDemark().isFraud())
                .toList();
    }
}
