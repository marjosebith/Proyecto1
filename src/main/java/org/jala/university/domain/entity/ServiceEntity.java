package org.jala.university.domain.entity;

// Jakarta Persistence: necesario para mapear la clase a una tabla de BD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Lombok: genera getters, setters, equals, hashCode automáticamente
import lombok.Builder;
import lombok.Data;

// Spring: registra automáticamente la fecha de creación y modificación
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

// BaseEntity del commons: obliga a implementar getId()
import org.jala.university.commons.domain.entity.BaseEntity;

import java.util.Date;
import java.util.UUID;

@Entity          // Le dice a JPA que esta clase es una tabla en BD
@Data            // Lombok genera todos los getters/setters
@Builder         // Permite construir objetos con ServiceEntity.builder().name("...").build()
public final class ServiceEntity implements BaseEntity<UUID> {

    @Id                                            // Este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // El ID se genera automáticamente como UUID
    UUID id;

    @Column                  // Mapea este campo como columna en la tabla
    String name;             // Nombre del servicio (ej: "Electricidad")

    @Column
    String description;      // Descripción del servicio

    @Column(name = "user_id") // En BD se llamará "user_id" — cumple criterios 2 y 3 de HU22
    UUID userId;              // ID del usuario al que se asocia el servicio

    @CreatedDate             // Spring asigna la fecha automáticamente al crear
    Date created;

    @LastModifiedDate        // Spring actualiza la fecha automáticamente al modificar
    Date updated;

    @Override
    public UUID getId() {    // Requerido por BaseEntity — devuelve el ID
        return id;
    }
}

