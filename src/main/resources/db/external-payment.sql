PRAGMA foreign_keys = ON; --// Habilitar claves foráneas en SQLite

CREATE TABLE users (
                       user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT UNIQUE NOT NULL,
                       email TEXT UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       full_name TEXT NOT NULL,
                       phone TEXT,
                       balance REAL DEFAULT 0.00,
                       is_active INTEGER DEFAULT 1,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_username ON users(username);

-- -----------------------------------------

CREATE TABLE services (
                          service_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          service_name TEXT NOT NULL,
                          service_type TEXT NOT NULL,
                          provider_name TEXT NOT NULL,
                          category TEXT,
--                           description TEXT,
                          is_active INTEGER DEFAULT 1,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_service_type ON services(service_type);
CREATE INDEX idx_provider ON services(provider_name);
CREATE INDEX idx_category ON services(category);

-- -----------------------------------------

CREATE TABLE user_services (
                               user_service_id INTEGER PRIMARY KEY AUTOINCREMENT,
                               user_id INTEGER NOT NULL,
                               service_id INTEGER NOT NULL,
                               account_number TEXT,
                               alias TEXT,
                               notes TEXT,
                               is_active INTEGER DEFAULT 1,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                               FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE RESTRICT,
                               UNIQUE (user_id, service_id, account_number)
);

CREATE INDEX idx_user ON user_services(user_id);

-- -----------------------------------------

CREATE TABLE payment_methods (
                                 payment_method_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                 method_name TEXT NOT NULL,
                                 method_code TEXT UNIQUE NOT NULL,
                                 description TEXT,
                                 is_active INTEGER DEFAULT 1,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------

CREATE TABLE recurring_payments (
                                    recurring_payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    user_id INTEGER NOT NULL,
                                    service_id INTEGER NOT NULL,
                                    payment_method_id INTEGER NOT NULL,

                                    frequency TEXT NOT NULL,
                                    next_execution_date DATE NOT NULL,
                                    amount REAL,
                                    is_dynamic_amount INTEGER DEFAULT 0,

                                    status TEXT NOT NULL,

                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    cancelled_at DATETIME,

                                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                    FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE RESTRICT,
                                    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(payment_method_id) ON DELETE RESTRICT
);

CREATE INDEX idx_rp_user ON recurring_payments(user_id);
CREATE INDEX idx_rp_status ON recurring_payments(status);
CREATE INDEX idx_next_execution ON recurring_payments(next_execution_date);

-- -----------------------------------------

CREATE TABLE payments (
                          payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          user_id INTEGER NOT NULL,
                          service_id INTEGER NOT NULL,
                          payment_method_id INTEGER NOT NULL,
                          recurring_payment_id INTEGER,

                          amount REAL NOT NULL,
                          reference_number TEXT UNIQUE NOT NULL,
                          transaction_id TEXT UNIQUE,

                          status TEXT NOT NULL,
                          payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,

                          notes TEXT,
                          error_message TEXT,

                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
                          FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE RESTRICT,
                          FOREIGN KEY (payment_method_id) REFERENCES payment_methods(payment_method_id) ON DELETE RESTRICT,
                          FOREIGN KEY (recurring_payment_id) REFERENCES recurring_payments(recurring_payment_id) ON DELETE SET NULL
);

CREATE INDEX idx_pay_user ON payments(user_id);
CREATE INDEX idx_status ON payments(status);
CREATE INDEX idx_payment_date ON payments(payment_date);
CREATE INDEX idx_reference ON payments(reference_number);

-- -----------------------------------------

CREATE TABLE invoices (
                          invoice_id INTEGER PRIMARY KEY AUTOINCREMENT,
                          payment_id INTEGER UNIQUE NOT NULL,
                          invoice_number TEXT UNIQUE NOT NULL,

                          user_name TEXT NOT NULL,
                          user_email TEXT NOT NULL,
                          service_name TEXT NOT NULL,
                          provider_name TEXT NOT NULL,

                          amount REAL NOT NULL,
                          payment_method TEXT NOT NULL,

                          invoice_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                          pdf_path TEXT,

                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (payment_id) REFERENCES payments(payment_id) ON DELETE RESTRICT
);

CREATE INDEX idx_invoice_number ON invoices(invoice_number);
CREATE INDEX idx_invoice_date ON invoices(invoice_date);

-- -----------------------------------------

CREATE TABLE payment_attempts (
                                  attempt_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                  recurring_payment_id INTEGER NOT NULL,

                                  attempted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  failure_reason TEXT,
                                  error_code TEXT,

                                  FOREIGN KEY (recurring_payment_id) REFERENCES recurring_payments(recurring_payment_id) ON DELETE CASCADE
);

CREATE INDEX idx_attempt_rp ON payment_attempts(recurring_payment_id);
CREATE INDEX idx_attempted_at ON payment_attempts(attempted_at);