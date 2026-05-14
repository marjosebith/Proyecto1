INSERT INTO users (full_name, email, password_hash, phone)
VALUES
    ('Ana rojas', 'ana.rojas@mail.com', '123456', '+591 70470002');

INSERT INTO services (service_name, service_type, provider_name, category)
VALUES
    ('Luz Residencial', 'utilities', 'CRE S.A.', 'Servicios Básicos'),
    ('Agua Potable', 'utilities', 'SAGUAPAC', 'Servicios Básicos'),
    ('Internet Fibra 100MB', 'telecom', 'Tigo Home', 'Telecomunicaciones'),
    ('Plan Postpago Ilimitado', 'telecom', 'Entel Bolivia', 'Telecomunicaciones'),
    ('Netflix Premium', 'entertainment', 'Netflix Inc.', 'Streaming');

INSERT INTO user_services (user_id, service_id, account_number, alias, notes) VALUES
                                                                                  (1, 1, '1234567890', 'Luz Casa', 'Pago mensual de luz'),
                                                                                  (1, 2, '0987654321', 'Agua Casa', 'Pago mensual de agua'),
                                                                                  (1, 3, '5555555555', 'Internet Casa', 'Pago mensual de internet');

INSERT INTO service_debts (user_id, user_service_id, billing_period, amount_due, due_date, status)
VALUES
(1, 1, '2026-05', 85000, '2026-05-25', 'PENDING'),
(1, 2, '2026-05', 42000, '2026-05-20', 'PENDING');

INSERT INTO payment_methods ( user_id, method_type, account_number, card_last_digits, balance)
VALUES
    (1,'BANK_ACCOUNT', 'ACC-987654321', NULL, 500000),
    (1, 'DEBIT_CARD', '5306917760014589', '4589', 250000);
