package br.com.zenon.presentation;

import br.com.zenon.models.ReportTransaction;

public class TransactionReportPresenter {
    public void display(ReportTransaction report) {
        IO.println("Total transactions: " + report.totalTransactions());
        IO.println("Total frauds: " + report.totalFrauds());
        IO.println("Total amount: " + report.totalAmount());
    }
}
