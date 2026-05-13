# Service Layer Pattern en el módulo External Payment

## Introducción

El patrón de diseño **Service Layer** consiste en crear una capa intermedia encargada de centralizar la lógica de 
negocio de la aplicación. Esta capa actúa como puente entre la interfaz de usuario y la capa de persistencia de datos.

Su principal objetivo es separar responsabilidades dentro del sistema, evitando que los controladores (`Controllers`) 
accedan directamente a la base de datos o implementen lógica de negocio compleja.

De esta manera:

- los controladores manejan únicamente la interacción con la interfaz,
- los repositorios gestionan el acceso a datos,
- y la capa de servicios coordina la lógica del sistema.

Este patrón es ampliamente utilizado en arquitecturas multicapa y aplicaciones empresariales debido a que mejora la 
mantenibilidad, reutilización y escalabilidad del software.

---

# Implementación del patrón en el módulo External Payment

En el módulo `external-payment-module`, el patrón Service Layer fue implementado mediante el paquete:

```text
org/jala/university/application/service
```

Dentro de esta capa se encontrarán servicios especializados que encapsulan las operaciones principales del sistema:

- `CreateService`
- `DeleteService`
- `GetService`
- `UpdateService`
- `SearchService`
- `GetTransactionHistory`

Cada uno de estos servicios se encarga de coordinar la lógica correspondiente a una funcionalidad específica del módulo.

---

# Funcionamiento dentro de la arquitectura

La arquitectura implementada sigue el siguiente flujo:

```text
Vista (FXML)
   ↓
Controller
   ↓
Service Layer
   ↓
Repository
   ↓
Base de Datos
```

Gracias a esta estructura:

- la interfaz gráfica no depende directamente de SQL,
- los controllers permanecen ligeros,
- y la lógica de negocio se mantiene centralizada.

---

# Evidencia del patrón en el proyecto

Uno de los ejemplos más claros del patrón se encuentra en la clase `UpdateService`.

```java
// Clase perteneciente a la capa Service Layer.
// Su responsabilidad es centralizar la lógica de negocio
// relacionada con la actualización de servicios del usuario.
public final class UpdateService {

    // El Service Layer NO accede directamente a SQL.
    // En cambio, delega el acceso a datos al Repository.
    private final UserServiceRepository repository;

    // Inyección de dependencia mediante constructor.
    // El servicio recibe el repositorio que utilizará.
    public UpdateService(UserServiceRepository repository) {
        this.repository = repository;
    }
    
    // Metodo encargado de obtener los servicios asociados al usuario.
    // Aquí el Service Layer valida primero la información
    // antes de delegar la operación al Repository.
    public List<UserService> getServicesByUser(int userId) {

        // Validación de lógica de negocio.
        // El controller NO realiza esta validación.
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID inválido");
        }

        // El Service Layer delega la consulta al Repository.
        // El repository es quien interactúa con la base de datos.
        return repository.findByUserId(userId);
    }

    // Metodo encargado de actualizar un servicio del usuario.
    public void update(UserService service) {

        // Validación centralizada en la capa service.
        // Esto evita lógica repetida en controllers.
        if (service.getAlias() == null || service.getAlias().isEmpty()) {
            throw new IllegalArgumentException("Alias requerido");
        }

        // El Service Layer coordina la operación
        // y delega la persistencia al Repository.
        repository.updateUserService(service);
    }
}
```

En este fragmento se puede observar que:

- la lógica de validación se encuentra en el servicio,
- el controller no interactúa directamente con la base de datos,
- y el acceso a persistencia se delega al repositorio.

---

# Beneficios obtenidos

## Separación de responsabilidades

Cada capa del sistema cumple una función específica:

- Controllers → interfaz
- Services → lógica de negocio
- Repository → persistencia

---

## Bajo acoplamiento

La lógica del sistema queda desacoplada de JavaFX y de SQL.

---

## Mayor mantenibilidad

Las modificaciones de negocio pueden realizarse en la capa service sin afectar la interfaz gráfica.

---

## Reutilización

Los servicios pueden ser reutilizados por distintos controllers o funcionalidades.

---

## Escalabilidad

La arquitectura facilita agregar nuevas funcionalidades sin modificar componentes existentes.

---

# Conclusión

La implementación del patrón Service Layer permitió organizar el módulo External Payment mediante una arquitectura 
más desacoplada y mantenible.

La lógica de negocio quedó centralizada dentro de la capa `org/jala/university/application/service`, mejorando la claridad del código y 
facilitando futuras extensiones del sistema.
