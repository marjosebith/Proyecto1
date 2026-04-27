# Funcionalidad: Editar Servicio Registrado

## 📋 Descripción

Esta funcionalidad permite a los usuarios editar la información de los servicios registrados en el sistema. Los usuarios pueden actualizar el alias, número de cuenta y notas de cada servicio sin perder la información histórica.

## ✅ Criterios de Aceptación Implementados

### 1. Se puede editar información
- ✅ Los usuarios pueden abrir la vista de edición haciendo clic en "Editar" 
- ✅ La información actual del servicio se carga automáticamente en el formulario
- ✅ Pueden modificar: Alias, Número de Cuenta y Notas
- ✅ La información del servicio base (nombre, proveedor, tipo) se muestra de forma solo-lectura

### 2. Se guarda correctamente
- ✅ Los datos se guardan en la BD tabla `user_services`
- ✅ Se actualiza el timestamp `updated_at` automáticamente
- ✅ La transacción es atómica (todo se guarda o nada se guarda)
- ✅ Confirmación visual al usuario tras guardar exitosamente

### 3. Se valida información ingresada
- ✅ **Alias**: Requerido, máximo 100 caracteres
- ✅ **Número de Cuenta**: Opcional, máximo 50 caracteres, solo alfanuméricos
- ✅ **Notas**: Opcional, máximo 500 caracteres
- ✅ Los errores se muestran en interfaz clara
- ✅ No se permite guardar con datos inválidos

### 4. Se refleja el cambio
- ✅ La tabla de servicios se actualiza automáticamente tras guardar
- ✅ Los cambios son visibles inmediatamente
- ✅ Se puede refrescar la tabla con el botón "Actualizar"
- ✅ Los cambios persisten en la base de datos

---

## 📁 Archivos Creados/Modificados

### Nuevos Archivos:

#### 1. **UserService.java** (Entidad)
```
src/main/java/org/jala/university/domain/entity/UserService.java
```
- Entidad modelo que representa un servicio registrado
- Contiene todos los atributos de la tabla `user_services`
- Incluye información del servicio base (nombre, proveedor, tipo)

#### 2. **UserServiceDto.java** (DTO)
```
src/main/java/org/jala/university/application/dto/UserServiceDto.java
```
- Data Transfer Object para transferencia de datos
- Facilita la comunicación entre capas

#### 3. **UserServiceMapper.java** (Mapper)
```
src/main/java/org/jala/university/application/mapper/UserServiceMapper.java
```
- Convierte entre entidades y DTOs
- Métodos: `toDto()`, `toEntity()`

#### 4. **UserServiceValidator.java** (Validador)
```
src/main/java/org/jala/university/application/validation/UserServiceValidator.java
```
- Valida los datos ingresados antes de guardar
- Métodos públicos:
  - `validate()`: Retorna lista de errores
  - `isValid()`: Retorna booleano

#### 5. **EditServiceController.java** (Controlador)
```
src/main/java/org/jala/university/presentation/controller/EditServiceController.java
```
- Controla la lógica de la vista de edición
- Métodos principales:
  - `initialize()`: Configura el controlador
  - `loadService(Long userServiceId)`: Carga datos del servicio
  - `handleSave()`: Valida y guarda cambios
  - `handleCancel()`: Cierra la ventana
  - `setOnServiceUpdated(Runnable)`: Callback para actualización

#### 6. **edit-service-view.fxml** (Vista)
```
src/main/resources/edit-service-view.fxml
```
- Interfaz para editar servicios
- Componentes:
  - 3 Labels de solo lectura (Servicio, Proveedor, Tipo)
  - TextField para Alias (requerido)
  - TextField para Número de Cuenta (opcional)
  - TextArea para Notas (opcional)
  - Botones: Guardar y Cancelar
  - Indicador de errores de validación

---

### Archivos Modificados:

#### 1. **ServicesDAO.java** (Data Access Object)
Nuevos métodos agregados:
- `getUserServices(Long userId)`: Obtiene todos los servicios de un usuario
- `getUserServiceById(Long userServiceId)`: Obtiene un servicio específico
- `updateUserService(UserService)`: Actualiza un servicio registrado
- `getAllServices()`: Obtiene todos los servicios disponibles
- `mapResultSetToUserService()`: Helper para mapear ResultSet

#### 2. **MainViewController.java** (Controlador Principal)
Modificaciones:
- Agregó TableView para mostrar servicios
- Configuración de columnas con datos
- Método `setupTable()`: Configura tabla y acciones
- Método `loadServices()`: Carga servicios desde BD
- Método `openEditServiceWindow()`: Abre diálogo de edición
- Método `handleRefresh()`: Recarga la tabla
- Método `setCurrentUserId()`: Permite cambiar usuario

#### 3. **main-view.fxml** (Vista Principal)
Cambios:
- Reemplazó diseño simple por tabla profesional
- Agregó TableView con 5 columnas
- Botón "Actualizar" para refrescar datos
- Botón "Editar" en cada fila
- Layout mejorado con BorderPane

---

## 🏗️ Arquitectura de la Solución

### Capas:

```
┌─────────────────────────────────────────────┐
│         PRESENTATION (GUI - JavaFX)         │
│  MainViewController | EditServiceController │
│  main-view.fxml | edit-service-view.fxml    │
└────────────┬────────────────────────────────┘
             │
┌────────────▼────────────────────────────────┐
│        APPLICATION (Business Logic)          │
│  UserServiceValidator | UserServiceMapper    │
│  ExternalPaymentService                     │
└────────────┬────────────────────────────────┘
             │
┌────────────▼────────────────────────────────┐
│         DOMAIN (Entities & DTOs)             │
│  UserService | UserServiceDto                │
└────────────┬────────────────────────────────┘
             │
┌────────────▼────────────────────────────────┐
│    INFRASTRUCTURE (Data Access - JDBC)       │
│  ServicesDAO | ConnectionManager             │
└────────────┬────────────────────────────────┘
             │
┌────────────▼────────────────────────────────┐
│         DATABASE (SQLite)                    │
│  user_services | services | users            │
└─────────────────────────────────────────────┘
```

---

## 💾 Esquema de Base de Datos

Tabla: `user_services`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `user_service_id` | INTEGER PRIMARY KEY AUTOINCREMENT | ID único |
| `user_id` | INTEGER NOT NULL | FK a tabla users |
| `service_id` | INTEGER NOT NULL | FK a tabla services |
| `account_number` | TEXT | Número de cuenta del servicio |
| `alias` | TEXT | Nombre personalizado del servicio |
| `notes` | TEXT | Notas adicionales |
| `is_active` | INTEGER DEFAULT 1 | Estado del registro |
| `created_at` | DATETIME DEFAULT CURRENT_TIMESTAMP | Fecha de creación |
| `updated_at` | DATETIME DEFAULT CURRENT_TIMESTAMP | Fecha de actualización |

---

## 🎯 Flujo de Ejecución

### 1. Cargar Servicios
```
MainViewController.initialize()
  ↓
setupTable() - Configura columnas y botones
  ↓
loadServices() - Obtiene servicios de BD
  ↓
ServicesDAO.getUserServices(userId)
  ↓
Tabla actualizada en pantalla
```

### 2. Editar Servicio
```
Usuario hace clic en "Editar"
  ↓
MainViewController.openEditServiceWindow()
  ↓
Carga edit-service-view.fxml
  ↓
EditServiceController.loadService(userServiceId)
  ↓
Datos se cargan en el formulario
```

### 3. Guardar Cambios
```
Usuario hace clic en "Guardar"
  ↓
EditServiceController.handleSave()
  ↓
UserServiceValidator.validate() - Valida datos
  ↓
ServicesDAO.updateUserService() - Actualiza BD
  ↓
Muestra mensaje de éxito
  ↓
Callback: MainViewController.loadServices()
  ↓
Tabla se actualiza automáticamente
  ↓
Ventana de edición se cierra
```

---

## 🧪 Validaciones

### Alias (Requerido)
- ✅ No puede estar vacío
- ✅ Máximo 100 caracteres
- ✅ Acepta cualquier carácter

### Número de Cuenta (Opcional)
- ✅ Máximo 50 caracteres (si se proporciona)
- ✅ Solo alfanuméricos y caracteres especiales: `-`, `_`, `.`
- ✅ Si está vacío, se guarda como NULL

### Notas (Opcional)
- ✅ Máximo 500 caracteres (si se proporciona)
- ✅ Si está vacío, se guarda como NULL

### Flujo de Validación
```
1. Usuario hace clic en "Guardar"
2. Se invocan validaciones
3. Si hay errores:
   - Se muestran en Label
   - Se abre AlertDialog
   - Se previene guardado
4. Si es válido:
   - Se actualiza BD
   - Se muestra confirmación
   - Se cierra ventana
```

---

## 🔒 Seguridad

- ✅ **Prepared Statements**: Uso de PreparedStatement para prevenir SQL Injection
- ✅ **Validación de Entrada**: Todas las entradas se validan antes de procesar
- ✅ **Transacciones Atómicas**: Los cambios son todos o nada
- ✅ **Foreign Keys**: Restricciones en BD mantienen integridad referencial
- ✅ **Só lectura de datos base**: No permite editar nombres de servicios

---

## 📝 Ejemplo de Uso

### 1. Iniciar la Aplicación
```java
MainViewController controller = new MainViewController();
controller.setCurrentUserId(1L);
```

### 2. Editar un Servicio
```
1. Ver tabla de servicios registrados
2. Hacer clic en botón "Editar" en la fila deseada
3. Se abre ventana de edición con datos cargados
4. Modificar Alias, Número de Cuenta y/o Notas
5. Hacer clic en "Guardar"
6. Sistema valida datos
7. Si es válido, guarda en BD y actualiza tabla
8. Ventana se cierra automáticamente
```

---

## 🧬 Dependencias Utilizadas

- **JavaFX 22**: Para la interfaz gráfica
- **Lombok**: Para anotaciones (@Data, @Builder, etc.)
- **SQLite JDBC**: Para acceso a base de datos
- **Java 17**: Runtime

---

## 📊 Estadísticas del Código

| Métrica | Valor |
|---------|-------|
| Archivos creados | 6 |
| Archivos modificados | 3 |
| Clases nuevas | 5 |
| Líneas de código | ~800+ |
| Métodos públicos | 15+ |
| Métodos privados | 10+ |
| Comentarios | 50+ |

---

## 🐛 Casos de Uso Cubiertos

1. ✅ Editar alias de servicio
2. ✅ Editar número de cuenta
3. ✅ Editar notas
4. ✅ Validar datos antes de guardar
5. ✅ Manejar errores de BD
6. ✅ Actualizar UI automáticamente
7. ✅ Cancelar edición sin guardar
8. ✅ Mostrar confirmaciones claras
9. ✅ Refrescar lista de servicios
10. ✅ Consultas eficientes a BD

---

## 🚀 Mejoras Futuras Posibles

1. Agregar pagina para crear nuevos servicios
2. Agregar función para eliminar servicios
3. Agregar búsqueda y filtrado en tabla
4. Exportar datos a Excel/PDF
5. Historial de cambios
6. Agregar campos adicionales (ej: contacto proveedor)
7. Confirmación de cambios críticos
8. Múltiples usuarios simultáneos
9. Auditoría de cambios por usuario
10. Undo/Redo de cambios

---

## 📞 Contacto y Soporte

Para preguntas o reportar problemas, contactar al equipo de desarrollo.

