# 🏗️ Arquitectura: Editar Servicio Registrado

## Diagrama de Componentes

```
┌──────────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN (GUI)                    │
│                         (JavaFX)                                 │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌─────────────────────┐                ┌───────────────────┐  │
│  │  main-view.fxml     │                │ MainViewController│  │
│  │  ─────────────────  │────────────────│ ─────────────────┤  │
│  │ • TableView         │    controla    │ • loadServices()  │  │
│  │ • 5 Columnas        │                │ • setupTable()    │  │
│  │ • Botón "Editar"    │                │ • handleRefresh() │  │
│  │ • Botón "Actualizar"│                └───────────────────┘  │
│  └─────────────────────┘                                        │
│           │                                                     │
│           │ abre                                                │
│           ▼                                                     │
│  ┌─────────────────────┐                ┌───────────────────┐  │
│  │edit-service-view    │                │EditServiceControl │  │
│  │  fxml               │────────────────│ ─────────────────┤  │
│  │ ─────────────────   │    controla    │ • loadService()   │  │
│  │ • 3 Labels RO       │                │ • handleSave()    │  │
│  │ • TextField Alias   │                │ • handleCancel()  │  │
│  │ • TextField Account │                └───────────────────┘  │
│  │ • TextArea Notas    │                                        │
│  │ • Botones Guardar   │                                        │
│  └─────────────────────┘                                        │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ invoca
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│                CAPA DE APLICACIÓN (Business Logic)               │
│                   (Service & Validation)                         │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│              ┌─────────────────────────────────┐                │
│              │ UserServiceValidator            │                │
│              │ ────────────────────────────    │                │
│              │ + validate()                    │                │
│              │ + isValid()                     │                │
│              └─────────────────────────────────┘                │
│                          ▲                                      │
│                          │                                      │
│  ┌─────────────────────┐ │    ┌──────────────────────────────┐ │
│  │ UserServiceMapper   │ │    │ExternalPaymentService (IF)   │ │
│  │ ───────────────────┤ │    │ ────────────────────────────  │ │
│  │ + toDto()          │ │    │ + doSomething()              │ │
│  │ + toEntity()       │ ├───▶│ + editService() [futura]     │ │
│  └─────────────────────┘ │    └──────────────────────────────┘ │
│                          │                                      │
│                          ▼                                      │
│              ┌─────────────────────────────────┐                │
│              │ ServicesDAO - Enhanced          │                │
│              │ ────────────────────────────    │                │
│              │ + getUserServices()             │                │
│              │ + getUserServiceById()          │                │
│              │ + updateUserService()           │                │
│              │ + getAllServices()              │                │
│              │ + mapResultSetToUserService()   │                │
│              └─────────────────────────────────┘                │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ uses JDBC
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│               CAPA DE DATOS (Data Access)                        │
│                    (JDBC Connection)                             │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│         ┌─────────────────────────────────────────┐             │
│         │ ConnectionManager                       │             │
│         │ ──────────────────────────────────────  │             │
│         │ + getConnection(): Connection          │             │
│         └─────────────────────────────────────────┘             │
│                          │                                      │
│                          │ jdbc:sqlite:db.sqlite3              │
│                          ▼                                      │
│         ┌─────────────────────────────────────────┐             │
│         │ SQLite Database (db.sqlite3)            │             │
│         └─────────────────────────────────────────┘             │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## Flujo de Datos: Edición de Servicio

```
┌─────────────┐
│ Usuario     │
│ hace clic   │
│ en "Editar" │
└──────┬──────┘
       │
       ▼
┌──────────────────────────────────────────┐
│ MainViewController.openEditServiceWindow()│
└──────┬───────────────────────────────────┘
       │ carga FXML
       ▼
┌──────────────────────────────────────────┐
│ edit-service-view.fxml                   │
│ + EditServiceController                  │
└──────┬───────────────────────────────────┘
       │ invoca initialize()
       ▼
┌──────────────────────────────────────────┐
│ EditServiceController.loadService(id)    │
│ ─────────────────────────────────────    │
│ • Obtiene datos del servicio             │
│ • Rellena formulario                     │
└──────┬───────────────────────────────────┘
       │
┌──────────────────────────────────────────┐
│ Usuario modifica datos                   │
│ • Alias                                  │
│ • Número de Cuenta                       │
│ • Notas                                  │
└──────┬───────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────┐
│ Usuario hace clic en "Guardar"           │
└──────┬───────────────────────────────────┘
       │ invoca handleSave()
       ▼
┌──────────────────────────────────────────┐
│ Valida datos                             │
│ • UserServiceValidator.validate()        │
└──────┬───────────────────────────────────┘
       │ ¿Válido?
       ├─────────NO─────────┐
       │                    ▼
       │        ┌──────────────────────────┐
       │        │ Muestra errores          │
       │        │ • Modal con errores      │
       │        │ • Label con errores      │
       │        │ • No permite guardar     │
       │        └──────────────────────────┘
       │
       ├──────SÍ──────┐
       │              ▼
       │   ┌──────────────────────────────┐
       │   │ ActualizaUserService         │
       │   │ en objeto                    │
       │   └──────┬───────────────────────┘
       │          │ invoca updateUserService()
       │          ▼
       │   ┌──────────────────────────────┐
       │   │ ServicesDAO                  │
       │   │ UPDATE user_services SET ... │
       │   │ PreparedStatement             │
       │   └──────┬───────────────────────┘
       │          │ JDBC
       │          ▼
       │   ┌──────────────────────────────┐
       │   │ SQLite inserta cambios       │
       │   │ • account_number             │
       │   │ • alias                      │
       │   │ • notes                      │
       │   │ • updated_at = NOW()         │
       │   └──────┬───────────────────────┘
       │          │ success?
       │          ├─────────NO─────────┐
       │          │                    ▼
       │          │        ┌─────────────────────┐
       │          │        │ Muestra error       │
       │          │        │ "No se pudo guardar"│
       │          │        └─────────────────────┘
       │          │
       │          ├─────────SÍ─────────┐
       │          │                    ▼
       │          │        ┌─────────────────────┐
       │          │        │ Muestra éxito       │
       │          │        │ "Actualizado OK"    │
       │          │        └──────┬──────────────┘
       │          │               │ cierra ventana
       │          │               ▼
       │          │        ┌─────────────────────┐
       │          │        │ Invoca callback     │
       │          │        │ onServiceUpdated()  │
       │          └────────┼─────────────────────┘
       │                   │
       │                   ▼
       │        ┌────────────────────────────────┐
       │        │MainViewController.loadServices()
       │        │ RE-CARGA TABLA                 │
       │        │ getUserServices()              │
       │        └────────────┬───────────────────┘
       │                     │
       │                     ▼
       │        ┌────────────────────────────────┐
       │        │ Tabla actualizada con          │
       │        │ nuevos datos                   │
       │        └────────────────────────────────┘
       │
       └────────────────────────────────────────►
```

---

## Estructura de Clases

```java
// CAPAS INFERIOR A SUPERIOR

// 1. DOMAIN LAYER - ENTIDADES
UserService {
    -userServiceId: Long
    -userId: Long
    -serviceId: Long
    -accountNumber: String
    -alias: String
    -notes: String
    -isActive: Integer
    -createdAt: LocalDateTime
    -updatedAt: LocalDateTime
    -serviceName: String
    -serviceType: String
    -providerName: String
}

// 2. DTO LAYER
UserServiceDto {
    -userServiceId: Long
    -userId: Long
    ...
}

// 3. APPLICATION LAYER
UserServiceValidator {
    +validate(): List<String>
    +isValid(): boolean
}

UserServiceMapper {
    +toDto(): UserServiceDto
    +toEntity(): UserService
}

// 4. INFRASTRUCTURE LAYER - DAO
ServicesDAO {
    +getUserServices(userId)
    +getUserServiceById(id)
    +updateUserService(service)
    +getAllServices()
}

// 5. PRESENTATION LAYER
MainViewController {
    +initialize()
    +setupTable()
    +loadServices()
    +openEditServiceWindow()
    +handleRefresh()
}

EditServiceController {
    +initialize()
    +loadService(id)
    +handleSave()
    +handleCancel()
}
```

---

## Diagrama de Bases de Datos (ERD)

```
┌─────────────┐
│   users     │
├─────────────┤
│ user_id (PK)│───┐
│ username    │   │
│ email       │   │
│ password    │   │
│ ...         │   │
└─────────────┘   │
                  │
                  │ 1:M
                  │
          ┌───────▼─────────┐
          │ user_services   │
          ├─────────────────┤
          │ user_service_id │(PK)
          │ user_id (FK) ◄──┘
          │ service_id (FK)───┐
          │ account_number    │
          │ alias             │ ◄── EDITABLE
          │ notes             │
          │ is_active         │
          │ created_at        │
          │ updated_at◄───────┤ AUTO UPDATE
          └───────────────────┘
                  │
                  │ M:1
                  │
          ┌───────▼─────────┐
          │  services       │
          ├─────────────────┤
          │ service_id (PK) │
          │ service_name    │
          │ service_type    │
          │ provider_name   │
          │ category        │
          │ is_active       │
          │ created_at      │
          │ updated_at      │
          └─────────────────┘

ÍNDICES:
- idx_user: (user_id)
- idx_service_type: (service_type)
- idx_provider: (provider_name)
- idx_category: (category)

UNIQUE:
- (user_id, service_id, account_number)
```

---

## Validaciones: Flow Diagram

```
┌──────────────────────┐
│ Entrada (3 campos)   │
│ • alias              │
│ • accountNumber      │
│ • notes              │
└──────────┬───────────┘
           │
           ▼
┌────────────────────────────────┐
│ Validar ALIAS                  │
├────────────────────────────────┤
│ ¿Vacío? → ERROR 1              │
│ ¿>100 chars? → ERROR 2         │
└──────────┬─────────────────────┘
           │
           ▼
┌────────────────────────────────┐
│ Validar ACCOUNT_NUMBER         │
├────────────────────────────────┤
│ ¿Es null/vacío? → OK (opcional)│
│ ¿>50 chars? → ERROR 3          │
│ ¿Contiene !alphanumeric? →     │
│   ERROR 4                       │
└──────────┬─────────────────────┘
           │
           ▼
┌────────────────────────────────┐
│ Validar NOTES                  │
├────────────────────────────────┤
│ ¿Es null/vacío? → OK (opcional)│
│ ¿>500 chars? → ERROR 5         │
└──────────┬─────────────────────┘
           │
           ▼
┌────────────────────────────────┐
│ ¿Errores encontrados?          │
└──────────┬─────────────────────┘
           │
    ┌──────┴──────┐
   SÍ            NO
    │             │
    ▼             ▼
  ERROR        ÉXITO
  Retorna    Retorna
  List<>     List<>
  (vacío)    (con errores)
```

---

## Performance & Optimization

```
┌─────────────────────────────────┐
│ Query Optimization              │
├─────────────────────────────────┤
│ SELECT us.*, s.* FROM ...       │
│                                 │
│ ÍNDICES UTILIZADOS:             │
│ • idx_user(user_id)             │
│ • PK user_services              │
│                                 │
│ TIEMPO ESTIMADO: < 1ms          │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│ Update Optimization             │
├─────────────────────────────────┤
│ UPDATE user_services SET ...    │
│ WHERE user_service_id = ?       │
│                                 │
│ PreparedStatement               │
│ + Previene SQL Injection        │
│ + Reutilizable                  │
│ + Optimizado por BD             │
│                                 │
│ TIEMPO ESTIMADO: < 5ms          │
└─────────────────────────────────┘
```

---

## Integración Completa

```
APPLICATION FLOW:
==================

1. START
   ↓
2. JavaFX LoadsMainView
   ↓
3. MainViewController.initialize()
   ├─ CreatesServicesDAO
   ├─ SetupTable()
   └─ LoadServices()
   ↓
4. ServicesDAO.getUserServices(1)
   └─ Query BD con JOIN
   ↓
5. TablaVisible with 3-5 Services
   ↓
6. User clicks Editar
   ↓
7. LoadsFXML: edit-service-view.fxml
   ├─ Loads EditServiceController
   └─ Injects @FXML fields
   ↓
8. EditServiceController.loadService(id)
   └─ ServicesDAO.getUserServiceById()
   ↓
9. Form populated with data
   ↓
10. User edits fields
    ↓
11. User clicks Guardar
    ↓
12. EditServiceController.handleSave()
    ├─ UserServiceValidator.validate()
    │  ├─ Check alias
    │  ├─ Check account
    │  └─ Check notes
    │
    └─ If valid:
       ├─ Service.update()
       └─ ServicesDAO.updateUserService()
          ├─ PreparedStatement
          ├─ BD commits
       ├─ Show success
       ├─ Close window
       └─ Callback: MainViewController.loadServices()
          └─ Table refreshed!

END
```

---

## Caso de Uso: Usuario Edita Alias de Servicio

```
ACTOR: Usuario
PRECONDICIÓN: Aplicación iniciada, servicios mostrados

1. Usuario ve tabla con servicios
2. Usuario selecciona servicio (ej: "Luz Casa")
3. Usuario hace clic botón "Editar"
4. Sistema abre ventana de edición
   - Muestra datos: "Luz Residencial", "Empresa Eléctrica Nacional"
   - Carga alias actual: "Luz Casa"
5. Usuario modifica alias: "Luz Casa Principal"
6. Usuario hace clic "Guardar"
7. Sistema valida:
   - Alias no vacío ✓
   - Alias < 100 caracteres ✓
8. Sistema actualiza BD
   - UPDATE user_services
   - SET alias = 'Luz Casa Principal', updated_at = NOW()
   - WHERE user_service_id = 1
9. Sistema cierra ventana de edición
10. Sistema recarga tabla
11. Tabla muestra nuevo alias: "Luz Casa Principal"
12. Usuario ve cambio reflejado inmediatamente

POST-CONDICIÓN: Servicio actualizado en BD y visible en UI
```

---

**Documento de Arquitectura - v1.0**  
**Generado: 25 de Abril de 2026**

