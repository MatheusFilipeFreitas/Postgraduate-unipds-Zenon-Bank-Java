package br.com.zenon.presentation;

import br.com.zenon.constants.Limits;
import br.com.zenon.models.Transaction;
import br.com.zenon.models.types.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudDashboardPresenter {
    public void display(List<Transaction> frauds) {
        displayTotalFrauds(frauds);
        displayTopFrauds(frauds, Limits.TOP_FRAUDS_DISPLAY, true);
        displayTopFraudClients(frauds, Limits.TOP_CLIENT_FRAUDS_DISPLAY, true);
        displayTotalAmountOfFrauds(frauds);
        displayTotalFraudsPeerType(frauds);
    }

    private void displayTotalFrauds(List<Transaction> frauds) {
        IO.println("Total Frauds: " + frauds.size());
    }

    private void displayTopFrauds(List<Transaction> frauds, int totalDisplayLimit, boolean isReversing) {
        Comparator<Transaction> comparator = buildComparator(Comparator.comparing(Transaction::amount), isReversing);
        List<BigDecimal> topFrauds = frauds.stream()
                .sorted(comparator)
                .map(Transaction::amount)
                .limit(totalDisplayLimit)
                .toList();

        IO.println("Top " + totalDisplayLimit + " Frauds with bigger amounts:");
        topFrauds.forEach(amount -> IO.println(formatMoney(amount)));
    }

    private void displayTopFraudClients(List<Transaction> frauds, int totalDisplayLimit, boolean isReversing) {
        Comparator<Transaction> comparator = buildComparator(Comparator.comparing(Transaction::amount), isReversing);

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

    private void displayTotalAmountOfFrauds(List<Transaction> frauds) {
        Comparator<Transaction> comparator = buildComparator(Comparator.comparing(Transaction::amount), true);
        BigDecimal total = frauds.stream()
                .sorted(comparator)
                .map(Transaction::amount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        IO.println("Total amount of Frauds: " + formatMoney(total));
    }

    private void displayTotalFraudsPeerType(List<Transaction> frauds) {
        frauds.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<TransactionType, Long>comparingByValue().reversed())
                .forEach(entry ->
                        IO.println(entry.getKey().toString().toUpperCase() + " - " + entry.getValue())
                );
    }

    private Comparator<Transaction> buildComparator(Comparator<Transaction> comparator, boolean isReversing) {
        if (isReversing) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private static String formatMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN).toPlainString();
    }
}
