package br.com.zenon.service;

import java.util.List;

public interface IAnalyzer<T> {
    void analyze(List<T> transactions);
    List<T> getFraudsFromTransactions(List<T> transactions);
}
