package br.com.zenon.utils.implementations;

import br.com.zenon.constants.Limits;
import br.com.zenon.models.Transaction;
import br.com.zenon.models.types.TransactionType;
import br.com.zenon.utils.IAnalyzer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class FraudAnalyzer implements IAnalyzer<Transaction> {
    private List<Transaction> frauds = new ArrayList<>();

    @Override
    public void analyze(List<Transaction> transactions) {
        frauds = getFraudsFromTransactions(transactions);
        displayDashboard();
    }

    @Override
    public List<Transaction> getFraudsFromTransactions(List<Transaction> transactions) {
        return transactions
                .stream()
                .filter(transaction -> transaction.fraudDemark().isFraud())
                .toList();
    }

    private void displayDashboard() {
        displayTotalFrauds();
        displayTopFrauds(Limits.TOP_FRAUDS_DISPLAY, true);
        displayTopFraudClients(Limits.TOP_CLIENT_FRAUDS_DISPLAY, true);
        displayTotalAmountOfFrauds();
        displayTotalFraudsPeerType();
    }

    private void displayTotalFrauds() {
        IO.println("Total Frauds: " + frauds.size());
    }

    private void displayTopFrauds(int totalDisplayLimit, boolean isRevesing) {
        Comparator<Transaction> comparator = buildComparator(Comparator.comparing(Transaction::amount), isRevesing);
        List<BigDecimal> topFrauds = frauds.stream()
                .sorted(comparator)
                .map(Transaction::amount)
                .limit(totalDisplayLimit)
                .toList();

        IO.println("Top " + totalDisplayLimit + " Frauds with bigger amounts:");
        topFrauds.forEach(amount -> IO.println(formatMoney(amount)));
    }

    private void displayTopFraudClients(int totalDisplayLimit, boolean isRevesing) {
        Comparator<Transaction> comparator = buildComparator(Comparator.comparing(Transaction::amount), isRevesing);

        List<String> topClientFrauds = frauds.stream()
                .sorted(comparator)
                .collect(Collectors.groupingBy(t -> t.origin().name(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(totalDisplayLimit)
                .map(Map.Entry::getKey)
                .toList();

        IO.println("Top clients with fraud: ");
        topClientFrauds.forEach(IO::println);
    }

    private void displayTotalAmountOfFrauds() {
        Comparator<Transaction> comparator = buildComparator(Comparator.comparing(Transaction::amount), true);
        BigDecimal total = frauds.stream()
                .sorted(comparator)
                .map(Transaction::amount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        IO.println("Total amount of Frauds: " + formatMoney(total));
    }

    private void displayTotalFraudsPeerType() {
        frauds.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<TransactionType, Long>comparingByValue().reversed())
                .forEach(entry ->
                        IO.println(entry.getKey().toString().toUpperCase() + " - " + entry.getValue())
                );
    }

    private Comparator<Transaction> buildComparator(Comparator<Transaction> comparator, boolean isRevesing) {
        if (isRevesing) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private static String formatMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN).toPlainString();
    }
}
