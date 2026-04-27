package org.jala.university.domain.repository;

// Importa la interfaz base del commons que ya tiene save, findById, findAll, delete
import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.ServiceEntity;

import java.util.UUID;

// Extiende Repository del commons con ServiceEntity como tipo y UUID como ID
// Por ahora no necesitas agregar métodos extra — save() ya viene incluido
public interface ServiceEntityRepository extends Repository<ServiceEntity, UUID> {
    // Si en el futuro necesitas "buscar por userId", lo agregas aquí
    // Ejemplo: List<ServiceEntity> findByUserId(UUID userId);
}
