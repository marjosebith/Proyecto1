PRAGMA foreign_keys = ON;

-- =========================================
-- TABLA: users
-- Usuarios del sistema
-- =========================================

CREATE TABLE users (

                       user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       full_name TEXT NOT NULL,
                       email TEXT NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL,
                       phone TEXT,

                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email ON users(email);
-- =========================================
-- TABLA: services
-- Catálogo global de entidades/servicios
-- =========================================

CREATE TABLE services (

                          service_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          service_name TEXT NOT NULL,
                          service_type TEXT NOT NULL,
                          provider_name TEXT NOT NULL,
                          category TEXT NOT NULL,

                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_service_type ON services(service_type);
CREATE INDEX idx_provider ON services(provider_name);
CREATE INDEX idx_category ON services(category);
-- =========================================
-- TABLA: user_services
-- Servicios registrados por el usuario
-- =========================================

CREATE TABLE user_services (

                               user_service_id INTEGER PRIMARY KEY AUTOINCREMENT,

                               user_id INTEGER NOT NULL,
                               service_id INTEGER NOT NULL,

                               account_number TEXT NOT NULL UNIQUE,
                               alias TEXT,
                               notes TEXT,

                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                               FOREIGN KEY (user_id)
                                       REFERENCES users(user_id)
                                       ON DELETE CASCADE,

                                   FOREIGN KEY (service_id)
                                       REFERENCES services(service_id)
                                       ON DELETE RESTRICT
);

CREATE INDEX idx_user ON user_services(user_id);
-- =========================================
-- TABLA: payment_methods
-- Métodos de pago del usuario
-- =========================================

CREATE TABLE payment_methods (

                                 payment_method_id INTEGER PRIMARY KEY AUTOINCREMENT,

                                 user_id INTEGER NOT NULL,

                                 method_type TEXT NOT NULL CHECK (
                                         method_type IN (
                                             'BANK_ACCOUNT',
                                             'CREDIT_CARD',
                                             'DEBIT_CARD',
                                             'CASH'
                                         )
                                     ),

                                 account_number TEXT,
                                 card_last_digits TEXT,

                                 balance REAL DEFAULT 0.00 CHECK(balance >= 0),
                                 is_active INTEGER DEFAULT 1 CHECK(is_active IN (0,1)),

                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                     FOREIGN KEY (user_id)
                                         REFERENCES users(user_id)
                                         ON DELETE CASCADE
);

CREATE INDEX idx_payment_methods_user ON payment_methods(user_id);
CREATE INDEX idx_payment_methods_active ON payment_methods(is_active);
-- =========================================
-- TABLA: recurring_payments
-- Pagos automáticos programados
-- =========================================

CREATE TABLE recurring_payments (

                                    recurring_payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    user_service_id INTEGER NOT NULL,
                                    payment_method_id INTEGER NOT NULL,

                                    frequency TEXT NOT NULL CHECK (
                                            frequency IN (
                                                'WEEKLY',
                                                'MONTHLY',
                                                'YEARLY'
                                            )
                                        ),

                                    next_execution_date TIMESTAMP NOT NULL,

                                    amount REAL NOT NULL CHECK(amount > 0),

                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    cancelled_at DATETIME,

                                    FOREIGN KEY (user_service_id)
                                        REFERENCES user_services(user_service_id) ON DELETE RESTRICT,

                                    FOREIGN KEY (payment_method_id)
                                        REFERENCES payment_methods(payment_method_id) ON DELETE RESTRICT
);
CREATE INDEX idx_rp_user_service_id ON recurring_payments(user_service_id);
CREATE INDEX idx_rp_next_execution ON recurring_payments(next_execution_date);
-- =========================================
-- TABLA: transactions
-- Historial de pagos
-- =========================================

CREATE TABLE transactions (

                              transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,

                              user_id INTEGER NOT NULL,

                              service_id INTEGER,
                              user_service_id INTEGER,
                              recurring_payment_id INTEGER,

                              payment_method_id INTEGER NOT NULL,

                              reference_code TEXT NOT NULL UNIQUE,

                              amount REAL NOT NULL CHECK(amount > 0),

                              status TEXT NOT NULL CHECK (
                                      status IN (
                                          'SUCCESS',
                                          'FAILED',
                                          'PENDING'
                                      )
                                  ),

                              payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              notes TEXT,

                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              FOREIGN KEY (user_id)
                                      REFERENCES users(user_id)
                                      ON DELETE RESTRICT,

                                  FOREIGN KEY (service_id)
                                      REFERENCES services(service_id)
                                      ON DELETE RESTRICT,

                                  FOREIGN KEY (user_service_id)
                                      REFERENCES user_services(user_service_id)
                                      ON DELETE RESTRICT,

                                  FOREIGN KEY (payment_method_id)
                                      REFERENCES payment_methods(payment_method_id)
                                      ON DELETE RESTRICT,

                                  FOREIGN KEY (recurring_payment_id)
                                      REFERENCES recurring_payments(recurring_payment_id)
                                      ON DELETE SET NULL
);

CREATE INDEX idx_transactions_user ON transactions(user_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_payment_date ON transactions(payment_date);
CREATE INDEX idx_transactions_reference ON transactions(reference_code);
CREATE INDEX idx_transactions_payment_method ON transactions(payment_method_id);

-- =========================================
-- TABLA: invoices
-- Facturas/comprobantes
-- =========================================

CREATE TABLE invoices (

                          invoice_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          transaction_id INTEGER NOT NULL,
                          invoice_number TEXT NOT NULL UNIQUE,

                          generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          file_path TEXT,

                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (transaction_id)
                              REFERENCES transactions(transaction_id) ON DELETE RESTRICT
);
CREATE INDEX idx_invoice_number ON invoices(invoice_number);
CREATE INDEX idx_invoice_date ON invoices(generated_at);
-- =========================================
-- TABLA: service_debts
-- agregar deuda a contratos
-- =========================================
CREATE TABLE service_debts (

    debt_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    user_service_id INTEGER NOT NULL,
    billing_period TEXT NOT NULL,

    amount_due REAL NOT NULL CHECK(amount_due >= 0),
    due_date DATE,

    status TEXT NOT NULL CHECK (
        status IN (
            'PENDING',
            'PAID',
            'OVERDUE'
        )
    ) DEFAULT 'PENDING',

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,


    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    FOREIGN KEY (user_service_id)
        REFERENCES user_services(user_service_id)
        ON DELETE CASCADE,

    UNIQUE(user_service_id, billing_period)
);
CREATE INDEX idx_user_services_account ON user_services(account_number);