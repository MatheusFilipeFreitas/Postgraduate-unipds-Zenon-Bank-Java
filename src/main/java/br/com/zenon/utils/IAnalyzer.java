package br.com.zenon.utils;

import br.com.zenon.models.Transaction;

import java.util.List;

public interface IAnalyzer<T> {
     void analyze(List<T> transactions);
     List<T> getFraudsFromTransactions(List<T> transactions);
}
