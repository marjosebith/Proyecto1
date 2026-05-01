# 🚀 Guía de Instalación y Uso: Funcionalidad de Editar Servicio

## 📋 Requisitos Previos

```
✓ Java 17 o superior
✓ Maven 3.8+
✓ Git (opcional)
✓ IDE: IntelliJ IDEA / Eclipse / VS Code (recomendado)
✓ SQLite 3 (incluido en JDBC)
```

---

## 📥 Instalación

### Paso 1: Descargar/Clonar Proyecto

```bash
# Si está en un repositorio
git clone <repository-url>
cd external-payment-module

# O si ya tiene el proyecto
cd /home/msplox/app/external-payment-module
```

### Paso 2: Compilar Proyecto

```bash
# Compilar y construir
mvn clean install

# O solo compilar sin tests
mvn clean compile
```

### Paso 3: Verificar Instalación

```bash
# Listar dependencias
mvn dependency:tree

# Verificar compilación
mvn checkstyle:check
```

---

## 🗄️ Configuración de Base de Datos

La aplicación usa SQLite automáticamente. No requiere configuración adicional.

```
Base de datos: db.sqlite3
Ubicación: /home/msplox/app/external-payment-module/

La base de datos se crea automáticamente en la primera ejecución
```

### Inicializar Base de Datos (si es necesario)

```bash
# Ejecutar script SQL
sqlite3 db.sqlite3 < SQL/external-payment.sql
```

---

## ▶️ Ejecutar la Aplicación

### Opción 1: Maven (Recomendado)

```bash
# Ejecutar directamente
mvn javafx:run

# O con compilación limpia
mvn clean javafx:run
```

### Opción 2: Desde IDE (IntelliJ IDEA)

```
1. File → Open → external-payment-module
2. Right click en pom.xml → Run Maven Build
3. Goals: javafx:run
4. Run
```

### Opción 3: Jar Ejecutable

```bash
# Compilar jar
mvn package

# Ejecutar
java -jar target/external-payment-module-1.0-SNAPSHOT.jar
```

---

## 🧪 Pruebas

### Ejecutar Todas las Pruebas

```bash
mvn test
```

### Ejecutar Test de Editar Servicio

```bash
# Correr test específico
mvn test -Dtest=TestEditService

# Con salida detallada
mvn test -Dtest=TestEditService -X
```

### Pruebas Manuales en la UI

```
1. Abre la aplicación
   mvn javafx:run

2. Verifica tabla de servicios:
   ✓ Debe mostrar "Luz Casa"
   ✓ Debe mostrar "Agua Casa"
   ✓ Debe mostrar "Internet Casa"

3. Prueba editar un servicio:
   a. Haz clic en "Editar" en la primera fila
   b. Modifica el alias: "Luz Principal"
   c. Haz clic en "Guardar"
   d. Verifica que la tabla se actualice
   e. Verifica en BD: sqlite3 db.sqlite3 "SELECT * FROM user_services"

4. Prueba validaciones:
   a. Abre edición
   b. Borra el alias completamente
   c. Haz clic en "Guardar"
   d. Debe mostrar error: "El alias es requerido"

5. Prueba número de cuenta:
   a. Intenta ingresar: "ABC@#$%"
   b. Debe mostrar error por caracteres inválidos

6. Prueba cancelar:
   a. Modifica datos
   b. Haz clic en "Cancelar"
   c. Verifica que NO se guardaron cambios
```

---

## 📁 Estructura del Proyecto

```
external-payment-module/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/jala/university/
│   │   │       ├── MainApp.java
│   │   │       ├── application/
│   │   │       │   ├── dao/
│   │   │       │   │   └── ServicesDAO.java (MODIFICADO)
│   │   │       │   ├── dto/
│   │   │       │   │   ├── UserServiceDto.java (NUEVO)
│   │   │       │   │   └── SampleEntityDto.java
│   │   │       │   ├── mapper/
│   │   │       │   │   ├── UserServiceMapper.java (NUEVO)
│   │   │       │   │   └── SampleEntityMapper.java
│   │   │       │   ├── service/
│   │   │       │   ├── validation/
│   │   │       │   │   └── UserServiceValidator.java (NUEVO)
│   │   │       │   └── model/
│   │   │       ├── db/
│   │   │       │   └── ConnectionManager.java
│   │   │       ├── domain/
│   │   │       │   ├── entity/
│   │   │       │   │   ├── UserService.java (NUEVO)
│   │   │       │   │   └── SampleEntity.java
│   │   │       │   └── repository/
│   │   │       ├── infrastructure/
│   │   │       └── presentation/
│   │   │           ├── ExternalPaymentView.java
│   │   │           ├── MainView.java
│   │   │           └── controller/
│   │   │               ├── MainViewController.java (MODIFICADO)
│   │   │               └── EditServiceController.java (NUEVO)
│   │   └── resources/
│   │       ├── main-view.fxml (MODIFICADO)
│   │       └── edit-service-view.fxml (NUEVO)
│   └── test/
│       └── java/
│           └── org/jala/university/
│               └── test/
│                   └── TestEditService.java (NUEVO)
├── SQL/
│   ├── external-payment.sql
│   └── DIAGRAMA.md
├── db.sqlite3
├── pom.xml
├── DOCUMENTACION_EDITAR_SERVICIO.md (NUEVO)
├── ARQUITECTURA_EDITAR_SERVICIO.md (NUEVO)
├── RESUMEN_IMPLEMENTACION.md (NUEVO)
└── README.md
```

---

## 🔧 Configuración de IDE

### IntelliJ IDEA

```
1. File → Project Structure
2. SDK: Java 17+
3. Language level: 17
4. Compiler: default

5. Run → Edit Configurations
6. New → Maven
   Name: Run JavaFX
   Command line: javafx:run
   Working directory: $PROJECT_DIR$
```

### Eclipse

```
1. Project → Properties
2. Compiler: Java 17+
3. Run → Maven Build
   Goals: javafx:run
```

---

## 🐛 Troubleshooting

### Problema: "No se puede encontrar la clase MainView"

```
Solución:
mvn clean compile
mvn javafx:run
```

### Problema: "SQLite JDBC no encontrado"

```
Solución:
mvn clean install
# O agregar manualmente:
mvn dependency:resolve
```

### Problema: "FXML no encuentra el controlador"

```
Verificar:
1. El package en @FXML sea correcto
2. El fx:controller esté bien escrito en el FXML
3. Compilar y ejecutar nuevamente:
   mvn clean javafx:run
```

### Problema: "No aparecen datos en la tabla"

```
Verificar:
1. La BD existe: ls db.sqlite3
2. Los datos se cargaron: sqlite3 db.sqlite3 "SELECT * FROM user_services"
3. El usuario ID 1 existe: sqlite3 db.sqlite3 "SELECT * FROM users WHERE user_id = 1"
4. Ejecutar el test: mvn test -Dtest=TestEditService
```

### Problema: Error al guardar cambios

```
Verificar en terminal:
sqlite3 db.sqlite3

Comprobar integridad:
PRAGMA integrity_check;

Ver estructura:
.schema user_services
```

---

## ✅ Checklist de Verificación

Después de instalar, verificar lo siguiente:

```
□ Proyecto compila sin errores
□ Tests pasan correctamente
□ Aplicación inicia sin crashes
□ Tabla muestra servicios
□ Botón "Editar" abre ventana
□ Campos de edición están habilitados
□ Botón "Guardar" guarda cambios
□ Tabla se actualiza después de guardar
□ Validaciones funcionan
□ Cambios persisten en BD
□ Botón "Actualizar" recarga tabla
□ Botón "Cancelar" cierra sin guardar
```

---

## 📊 Verificar Datos en BD

```bash
# Conectar a SQLite
sqlite3 db.sqlite3

# Ver usuarios
SELECT * FROM users;

# Ver servicios registrados del usuario 1
SELECT us.user_service_id, us.alias, s.service_name, us.updated_at
FROM user_services us
JOIN services s ON us.service_id = s.service_id
WHERE us.user_id = 1;

# Ver cambios recientes
SELECT * FROM user_services 
ORDER BY updated_at DESC 
LIMIT 5;

# Salir
.exit
```

---

## 📝 Logs y Debugging

### Ver logs de aplicación

```bash
# Con nivel DEBUG
mvn javafx:run -X

# Solo compilación
mvn compile -X
```

### Habilitar logs en código

Agregar en código si es necesario:

```java
System.out.println("DEBUG: " + mensaje);
System.err.println("ERROR: " + error);
```

---

## 🚀 Deployment

### Crear distribuciónes

```bash
# Clean build
mvn clean package

# Con tests
mvn clean package -DskipTests=false

# JAR executable
ls target/external-payment-module-1.0-SNAPSHOT.jar

# Ejecutar el JAR
java -jar target/external-payment-module-1.0-SNAPSHOT.jar
```

---

## 📞 Soporte

En caso de problemas:

1. Verificar logs: `mvn clean javafx:run -X`
2. Revisar documentación: `DOCUMENTACION_EDITAR_SERVICIO.md`
3. Ver arquitectura: `ARQUITECTURA_EDITAR_SERVICIO.md`
4. Ejecutar test: `mvn test -Dtest=TestEditService`

---

## 📚 Recursos Adicionales

- [JavaFX Official Docs](https://openjfx.io/)
- [SQLite JDBC](https://github.com/xerial/sqlite-jdbc)
- [Maven Guide](https://maven.apache.org/)
- [Java 17 Features](https://docs.oracle.com/en/java/javase/17/)

---

**Última actualización: 25 de Abril de 2026**  
**Versión: 1.0-SNAPSHOT**  
**Estado: LISTO PARA USO**

