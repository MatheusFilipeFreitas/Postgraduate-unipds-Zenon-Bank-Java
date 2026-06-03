-- Runs once on first container startup (docker-entrypoint-initdb.d).
-- Schema aligned with br.com.zenon.models.Transaction, Origin, FraudDemark, TransactionType.

CREATE TYPE transaction_type AS ENUM (
    'CASH_IN',
    'CASH_OUT',
    'DEBIT',
    'PAYMENT',
    'TRANSFER'
);

CREATE TABLE transactions (
    id                  BIGSERIAL PRIMARY KEY,
    step                INTEGER NOT NULL CHECK (step >= 1),
    type                transaction_type NOT NULL,
    amount              NUMERIC(19, 2) NOT NULL CHECK (amount >= 0),

    name_orig           VARCHAR(255) NOT NULL,
    old_balance_orig    NUMERIC(19, 2) NOT NULL CHECK (old_balance_orig >= 0),
    new_balance_orig    NUMERIC(19, 2) NOT NULL CHECK (new_balance_orig >= 0),

    name_dest           VARCHAR(255) NOT NULL,
    old_balance_dest    NUMERIC(19, 2) NOT NULL CHECK (old_balance_dest >= 0),
    new_balance_dest    NUMERIC(19, 2) NOT NULL CHECK (new_balance_dest >= 0),

    is_fraud            BOOLEAN NOT NULL DEFAULT FALSE,
    is_flagged_fraud    BOOLEAN NOT NULL DEFAULT FALSE,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transactions_name_orig ON transactions (name_orig);
CREATE INDEX idx_transactions_is_fraud ON transactions (is_fraud);
CREATE INDEX idx_transactions_type ON transactions (type);
