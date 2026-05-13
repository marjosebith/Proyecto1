INSERT INTO users (username, email, password_hash, full_name, phone, balance) VALUES
    ('juan.perez', 'perez2004@gmail.com', '12345678', 'juan jose perez gomez', '300-855-4578', '10000.00');

INSERT INTO payment_methods (method_name, method_code, description) VALUES
                                                                        ('Saldo de Cuenta', 'balance', 'Pago usando el saldo disponible en la cuenta'),
                                                                        ('Tarjeta de Crédito', 'credit_card', 'Pago con tarjeta de crédito'),
                                                                        ('Tarjeta de Débito', 'debit_card', 'Pago con tarjeta de débito'),
                                                                        ('Transferencia Bancaria', 'bank_transfer', 'Transferencia desde cuenta bancaria');

INSERT INTO services (service_name, service_type, provider_name, category, description) VALUES
                                                                               ('Luz Residencial', 'utilities', 'Empresa Eléctrica Nacional', 'Servicios Básicos', 'Pago de energía eléctrica'),
                                                                               ('Agua Potable', 'utilities', 'Servicio de Agua Municipal', 'Servicios Básicos', 'Servicio de agua para el hogar'),
                                                                               ('Internet Fibra Óptica', 'telecom', 'TeleCom Plus', 'Telecomunicaciones', 'Internet de alta velocidad'),
                                                                               ('Telefonía Móvil', 'telecom', 'MovilNet', 'Telecomunicaciones', 'Servicio de telefonía celular'),
                                                                               ('Gas Natural', 'utilities', 'Gas del Estado', 'Servicios Básicos', 'Suministro de gas domiciliario'),
                                                                               ('TV por Cable', 'entertainment', 'CableVision', 'Entretenimiento', 'Servicio de televisión por suscripción');

INSERT INTO user_services (user_id, service_id, account_number, alias, notes) VALUES
                                                                                  (1, 1, '1234567890', 'Luz Casa', 'Pago mensual de luz'),
                                                                                  (1, 2, '0987654321', 'Agua Casa', 'Pago mensual de agua'),
                                                                                  (1, 3, '5555555555', 'Internet Casa', 'Pago mensual de internet');
