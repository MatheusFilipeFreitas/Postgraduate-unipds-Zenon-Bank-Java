package br.com.zenon.repository.queries;

public class TransactionQuery {
    public static final String INSERT = """
            INSERT INTO transactions (
                step, type, amount,
                name_orig, old_balance_orig, new_balance_orig,
                name_dest, old_balance_dest, new_balance_dest,
                is_fraud, is_flagged_fraud
            ) VALUES (?, ?::transaction_type, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    public static final String FIND_BY_ORIGIN = """
            SELECT id, step, type, amount,
                   name_orig, old_balance_orig, new_balance_orig,
                   name_dest, old_balance_dest, new_balance_dest,
                   is_fraud, is_flagged_fraud, created_at
            FROM transactions
            WHERE name_orig = ?
            """;
}
