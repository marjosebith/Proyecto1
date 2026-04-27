# Documentación del Sistema de Pagos Externos - Base de Datos

## Tabla de Contenidos
1. [Descripción General](#descripción-general)
2. [Arquitectura de la Base de Datos](#arquitectura-de-la-base-de-datos)
3. [Tablas del Sistema](#tablas-del-sistema)
4. [Relaciones entre Tablas](#relaciones-entre-tablas)
5. [Índices](#índices)
6. [Restricciones y Validaciones](#restricciones-y-validaciones)

---

## Descripción General

Este sistema de base de datos está diseñado para gestionar pagos externos de servicios básicos, telecomunicaciones y entretenimiento. Permite a los usuarios registrar sus servicios, configurar pagos recurrentes y mantener un historial completo de transacciones con generación de facturas.

**Características principales:**
- ✅ Gestión de usuarios con saldos de cuenta
- ✅ Catálogo de servicios y proveedores
- ✅ Múltiples métodos de pago
- ✅ Pagos recurrentes automatizados
- ✅ Sistema de intentos de pago con registro de fallos
- ✅ Generación de facturas (invoices)
- ✅ Auditoría completa de transacciones

---

## Arquitectura de la Base de Datos

```
┌─────────────────────────────────────────────────────┐
│                     SISTEMA DE PAGOS                │
└─────────────────────────────────────────────────────┘
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
   ┌────▼────┐      ┌────▼────┐      ┌────▼────┐
   │  USERS  │      │SERVICES │      │PAYMENT  │
   │         │      │         │      │METHODS  │
   └────┬────┘      └────┬────┘      └────┬────┘
        │                │                │
        └────────────────┼────────────────┘
                         │
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────▼─────┐  ┌─────▼────┐  ┌──────▼───┐
    │ PAYMENTS │  │ RECURRING│  │USER      │
    │          │  │ PAYMENTS │  │SERVICES  │
    └────┬─────┘  └─────┬────┘  └──────────┘
         │               │
         │       ┌───────┼────────┐
         │       │               │
    ┌────▼───────▼─┐      ┌─────▼─────┐
    │   INVOICES   │      │  PAYMENT  │
    │              │      │  ATTEMPTS │
    └──────────────┘      └───────────┘
```

---

## Tablas del Sistema

### 1. **users** - Gestión de Usuarios
Almacena toda la información de los usuarios del sistema.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `user_id` | INTEGER | PK, AUTOINCREMENT | Identificador único del usuario |
| `username` | TEXT | UNIQUE, NOT NULL | Nombre de usuario único |
| `email` | TEXT | UNIQUE, NOT NULL | Correo electrónico único |
| `password_hash` | TEXT | NOT NULL | Hash criptográfico de la contraseña |
| `full_name` | TEXT | NOT NULL | Nombre completo del usuario |
| `phone` | TEXT | NULL | Número telefónico |
| `balance` | REAL | DEFAULT 0.00 | Saldo disponible en la cuenta |
| `is_active` | INTEGER | DEFAULT 1 | Estado de actividad (1=Activo, 0=Inactivo) |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |
| `updated_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha última actualización |

**Índices:**
- `idx_email` - búsqueda rápida por correo
- `idx_username` - búsqueda rápida por usuario

---

### 2. **services** - Catálogo de Servicios
Define los servicios disponibles en el sistema (luz, agua, internet, etc.).

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `service_id` | INTEGER | PK, AUTOINCREMENT | Identificador único del servicio |
| `service_name` | TEXT | NOT NULL | Nombre del servicio (e.g., "Luz Residencial") |
| `service_type` | TEXT | NOT NULL | Tipo de servicio (utilities, telecom, entertainment) |
| `provider_name` | TEXT | NOT NULL | Nombre del proveedor |
| `category` | TEXT | NULL | Categoría del servicio |
| `description` | TEXT | NULL | Descripción detallada |
| `is_active` | INTEGER | DEFAULT 1 | Estado de disponibilidad |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |
| `updated_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha última actualización |

**Servicios predefinidos incluidos:**
- Luz Residencial (Empresa Eléctrica Nacional)
- Agua Potable (Servicio de Agua Municipal)
- Internet Fibra Óptica (TeleCom Plus)
- Telefonía Móvil (MovilNet)
- Gas Natural (Gas del Estado)
- TV por Cable (CableVision)

**Índices:**
- `idx_service_type` - búsqueda por tipo de servicio
- `idx_provider` - búsqueda por proveedor
- `idx_category` - búsqueda por categoría

---

### 3. **user_services** - Servicios del Usuario
Relación entre usuarios y sus servicios contratados.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `user_service_id` | INTEGER | PK, AUTOINCREMENT | Identificador único |
| `user_id` | INTEGER | FK → users, NOT NULL | Referencia al usuario |
| `service_id` | INTEGER | FK → services, NOT NULL | Referencia al servicio |
| `account_number` | TEXT | NULL | Número de cuenta/cliente del usuario |
| `alias` | TEXT | NULL | Alias personalizado (e.g., "Mi luz") |
| `notes` | TEXT | NULL | Notas adicionales |
| `is_active` | INTEGER | DEFAULT 1 | Estado de actividad |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |
| `updated_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha última actualización |

**Restricciones especiales:**
- UNIQUE(user_id, service_id, account_number) - Evita duplicados
- ON DELETE CASCADE - Si se elimina el usuario, se eliminan sus servicios

**Índices:**
- `idx_user` - búsqueda rápida de servicios por usuario

---

### 4. **payment_methods** - Métodos de Pago
Define los métodos de pago disponibles en el sistema.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `payment_method_id` | INTEGER | PK, AUTOINCREMENT | Identificador único |
| `method_name` | TEXT | NOT NULL | Nombre del método (e.g., "Tarjeta de Crédito") |
| `method_code` | TEXT | UNIQUE, NOT NULL | Código único (e.g., "credit_card") |
| `description` | TEXT | NULL | Descripción del método |
| `is_active` | INTEGER | DEFAULT 1 | Estado de disponibilidad |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |

**Métodos predefinidos:**
1. **Saldo de Cuenta** (balance) - Pago usando saldo disponible
2. **Tarjeta de Crédito** (credit_card) - Pago con tarjeta de crédito
3. **Tarjeta de Débito** (debit_card) - Pago con tarjeta de débito
4. **Transferencia Bancaria** (bank_transfer) - Transferencia bancaria directa

---

### 5. **recurring_payments** - Pagos Recurrentes
Gestiona los pagos automáticos programados de los usuarios.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `recurring_payment_id` | INTEGER | PK, AUTOINCREMENT | Identificador único |
| `user_id` | INTEGER | FK → users, NOT NULL | Usuario propietario del pago |
| `service_id` | INTEGER | FK → services, NOT NULL | Servicio a pagar |
| `payment_method_id` | INTEGER | FK → payment_methods, NOT NULL | Método de pago a usar |
| `frequency` | TEXT | NOT NULL | Frecuencia de pago (daily, weekly, monthly, yearly) |
| `next_execution_date` | DATE | NOT NULL | Próxima fecha de ejecución |
| `amount` | REAL | NULL | Monto a pagar (si es fijo) |
| `is_dynamic_amount` | INTEGER | DEFAULT 0 | Si 1, el monto se calcula dinamicamente |
| `status` | TEXT | NOT NULL | Estado (active, paused, cancelled) |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |
| `updated_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Última actualización |
| `cancelled_at` | DATETIME | NULL | Fecha de cancelación (si aplica) |

**Estados de pago:**
- `active` - Pagos en ejecución
- `paused` - Pagos pausados temporalmente
- `cancelled` - Pagos cancelados

**Frecuencias soportadas:**
- `daily` - Diariamente
- `weekly` - Semanalmente
- `monthly` - Mensualmente
- `yearly` - Anualmente

**Índices:**
- `idx_rp_user` - búsqueda de pagos recurrentes por usuario
- `idx_rp_status` - búsqueda por estado
- `idx_next_execution` - búsqueda por próxima ejecución (importante para scheduler)

---

### 6. **payments** - Historial de Pagos
Registro completo de todas las transacciones realizadas.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `payment_id` | INTEGER | PK, AUTOINCREMENT | Identificador único |
| `user_id` | INTEGER | FK → users, NOT NULL | Usuario que realizó el pago |
| `service_id` | INTEGER | FK → services, NOT NULL | Servicio pagado |
| `payment_method_id` | INTEGER | FK → payment_methods, NOT NULL | Método utilizado |
| `recurring_payment_id` | INTEGER | FK → recurring_payments, NULL | Referencia si es pago automático |
| `amount` | REAL | NOT NULL | Monto pagado |
| `reference_number` | TEXT | UNIQUE, NOT NULL | Número de referencia único |
| `transaction_id` | TEXT | UNIQUE, NULL | ID de transacción del proveedor |
| `status` | TEXT | NOT NULL | Estado (pending, completed, failed, cancelled) |
| `payment_date` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha del pago |
| `notes` | TEXT | NULL | Notas adicionales |
| `error_message` | TEXT | NULL | Mensaje de error si falla |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de registro |
| `updated_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Última actualización |

**Estados de transacción:**
- `pending` - Pago pendiente
- `completed` - Pago completado exitosamente
- `failed` - Pago rechazado
- `cancelled` - Pago cancelado

**Índices:**
- `idx_pay_user` - búsqueda de pagos por usuario
- `idx_status` - búsqueda por estado
- `idx_payment_date` - búsqueda por rango de fechas
- `idx_reference` - búsqueda por número de referencia

---

### 7. **invoices** - Facturas
Almacena las facturas generadas para cada transacción completada.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `invoice_id` | INTEGER | PK, AUTOINCREMENT | Identificador único |
| `payment_id` | INTEGER | FK → payments, UNIQUE, NOT NULL | Asociado a un pago específico |
| `invoice_number` | TEXT | UNIQUE, NOT NULL | Número de factura |
| `user_name` | TEXT | NOT NULL | Nombre del usuario |
| `user_email` | TEXT | NOT NULL | Email para envío |
| `service_name` | TEXT | NOT NULL | Nombre del servicio |
| `provider_name` | TEXT | NOT NULL | Nombre del proveedor |
| `amount` | REAL | NOT NULL | Monto facturado |
| `payment_method` | TEXT | NOT NULL | Método de pago usado |
| `invoice_date` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de la factura |
| `pdf_path` | TEXT | NULL | Ruta del archivo PDF generado |
| `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación |

**Índices:**
- `idx_invoice_number` - búsqueda rápida por número de factura
- `idx_invoice_date` - búsqueda por rango de fechas

---

### 8. **payment_attempts** - Intentos de Pago
Registro de intentos fallidos de ejecución de pagos recurrentes para auditoría.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| `attempt_id` | INTEGER | PK, AUTOINCREMENT | Identificador único |
| `recurring_payment_id` | INTEGER | FK → recurring_payments, NOT NULL | Pago recurrente asociado |
| `attempted_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha del intento |
| `failure_reason` | TEXT | NULL | Razón del fallo |
| `error_code` | TEXT | NULL | Código de error |

**Propósito:** Mantener un historial de todos los intentos fallidos para ayudar en el diagnóstico y reintentos.

**Índices:**
- `idx_attempt_rp` - búsqueda de intentos por pago recurrente
- `idx_attempted_at` - búsqueda por rango de fechas de intentos

---

## Relaciones entre Tablas

### Diagrama de Relaciones (ER-Modelo)

```
┌──────────────┐
│    USERS     │
│──────────────│
│ user_id (PK) │
└──────────────┘
       │
       ├─────────────────────────────────────────┐
       │                    │                    │
       │(1:N)          │(1:N)                │(1:N)
       ▼                ▼                    ▼
┌────────────────┐  ┌──────────────┐  ┌──────────────┐
│ USER_SERVICES  │  │  PAYMENTS    │  │RECURRING_PAY │
└────────────────┘  └──────────────┘  └──────────────┘
       │                   │                 │
       │              (1:1)│                 │
       ├─────────────┐     │                 │
       │        (FK) │     └──────┬──────────┘
       ▼            ▼            ▼
┌──────────────┐  ┌────────────┐  ┌─────────────────┐
│  SERVICES    │  │ INVOICES   │  │ PAYMENT_ATTEMPTS│
└──────────────┘  └────────────┘  └─────────────────┘
       │
    (1:N)
       │
    (FK)
       │
       ▼
┌─────────────────────┐
│ PAYMENT_METHODS     │
└─────────────────────┘
```

### Flujo de Relaciones Clave:

1. **Users → User_Services ← Services**
   - Relación N:M entre usuarios y servicios
   - Permite que un usuario tenga múltiples servicios
   
2. **Users → Payments ← Services**
   - Registra cada transacción
   - Vincula usuario, servicio y método de pago
   
3. **Payments → Invoices** (1:1)
   - Cada pago completo genera una factura
   
4. **Recurring_Payments → Payments**
   - Los pagos automáticos se registran como pagos normales
   
5. **Recurring_Payments → Payment_Attempts**
   - Registra cada intento (exitoso o fallido) de ejecución

---

## Índices

Todos los índices están optimizados para mejorar el rendimiento de las consultas más frecuentes:

| Tabla | Índice | Campo(s) | Propósito |
|-------|--------|----------|----------|
| users | `idx_email` | email | Búsqueda de usuario por correo |
| users | `idx_username` | username | Búsqueda de usuario por nombre |
| services | `idx_service_type` | service_type | Filtrar servicios por tipo |
| services | `idx_provider` | provider_name | Búsqueda por proveedor |
| services | `idx_category` | category | Clasificación de servicios |
| user_services | `idx_user` | user_id | Obtener servicios de un usuario |
| recurring_payments | `idx_rp_user` | user_id | Pagos recurrentes del usuario |
| recurring_payments | `idx_rp_status` | status | Filtrar por estado |
| recurring_payments | `idx_next_execution` | next_execution_date | **CRÍTICO** para scheduler |
| payments | `idx_pay_user` | user_id | Historial de pagos del usuario |
| payments | `idx_status` | status | Filtrar pagos por estado |
| payments | `idx_payment_date` | payment_date | Búsqueda por rango de fechas |
| payments | `idx_reference` | reference_number | Búsqueda por referencia |
| invoices | `idx_invoice_number` | invoice_number | Búsqueda rápida de factura |
| invoices | `idx_invoice_date` | invoice_date | Búsqueda por período |
| payment_attempts | `idx_attempt_rp` | recurring_payment_id | Historial de intentos |
| payment_attempts | `idx_attempted_at` | attempted_at | Búsqueda por período |

---

## Restricciones y Validaciones

### Integridad Referencial

1. **Claves Foráneas (Foreign Keys):**
   - Todas las referencias de usuarios son **ON DELETE CASCADE** (eliminación en cascada)
   - Referencias a servicios y métodos de pago son **ON DELETE RESTRICT** (previene eliminación si hay datos relacionados)
   - Excepto `recurring_payment_id` en payments que es **ON DELETE SET NULL**

2. **Unicidad (UNIQUE):**
   - `username` - Cada usuario tiene nombre único
   - `email` - Cada usuario tiene correo único
   - `method_code` - Cada método de pago tiene código único
   - `reference_number` - Cada pago tiene referencia única
   - `transaction_id` - ID de transacción es único (cuando disponible)
   - `invoice_number` - Cada factura tiene número único
   - `payment_id` en invoices - Una factura por pago
   - Combinación (user_id, service_id, account_number) - Evita servicios duplicados

### PRAGMA de Base de Datos

```sql
PRAGMA foreign_keys = ON;
```
- Activa la verificación de claves foráneas (recomendado para integridad)

### Auditoría Automática

Todas las tablas principales incluyen campos de auditoría:
- `created_at` - Marca de tiempo de creación (automática)
- `updated_at` - Marca de tiempo de última modificación (automática)
- `cancelled_at` - Marca de tiempo de cancelación (manual, cuando aplica)

---

## Consultas Típicas Recomendadas

### Obtener servicios de un usuario
```sql
SELECT s.service_name, s.provider_name, us.account_number, us.alias
FROM user_services us
JOIN services s ON us.service_id = s.service_id
WHERE us.user_id = ? AND us.is_active = 1;
```

### Próximos pagos recurrentes a ejecutar (máximo 1 hora)
```sql
SELECT rp.*, u.email, s.service_name
FROM recurring_payments rp
JOIN users u ON rp.user_id = u.user_id
JOIN services s ON rp.service_id = s.service_id
WHERE rp.status = 'active' 
  AND DATE(rp.next_execution_date) <= DATE('now')
ORDER BY rp.next_execution_date ASC;
```

### Historial completo de pagos de un usuario
```sql
SELECT p.reference_number, p.amount, s.service_name, pm.method_name, p.status, p.payment_date
FROM payments p
JOIN services s ON p.service_id = s.service_id
JOIN payment_methods pm ON p.payment_method_id = pm.payment_method_id
WHERE p.user_id = ?
ORDER BY p.payment_date DESC;
```

### Intentos fallidos de pago recurrente
```sql
SELECT pa.*, rp.service_id, rp.user_id
FROM payment_attempts pa
JOIN recurring_payments rp ON pa.recurring_payment_id = rp.recurring_payment_id
WHERE pa.failure_reason IS NOT NULL
ORDER BY pa.attempted_at DESC
LIMIT 100;
```

---

## Notas de Implementación

1. **Transacciones:** Se recomienda usar transacciones para operaciones críticas (ej: actualizar balance y registrar pago)

2. **Bloqueos:** Para pagos concurrentes, considerar mecanismos de bloqueo optimista

3. **Limpieza:** Implementar purgas periódicas de `payment_attempts` para mantener la base de datos eficiente

4. **Backups:** Realizar backups regulares especialmente antes de operaciones de pago masivas

5. **Logging:** Todas las transacciones financieras deben registrase en logs de auditoría

---

## Historial de Cambios

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0 | 2026-04-24 | Creación inicial del esquema |

---

**Generado automáticamente por el sistema de documentación**
