package br.com.zenon.presentation.implementation;

import br.com.zenon.models.ReportTransaction;

public class TransactionReportPresenter extends Presenter {
    public void display(ReportTransaction report) {
        displayMessageFromKey("report.transactions");
        IO.println(report.totalTransactions());
        displayMessageFromKey("report.frauds");
        IO.println(report.totalFrauds());
        displayMessageFromKey("report.amounts");
        IO.println(report.totalAmount());
    }
}
