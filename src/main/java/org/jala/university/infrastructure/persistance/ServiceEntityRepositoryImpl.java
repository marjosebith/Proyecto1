package org.jala.university.infrastructure.persistance;

// EntityManager es el puente entre Java y la base de datos (viene de JPA)
import jakarta.persistence.EntityManager;

// CrudRepository del commons ya implementa save, findById, findAll, delete
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.ServiceEntity;
import org.jala.university.domain.repository.ServiceEntityRepository;

import java.util.UUID;

// Hereda toda la lógica CRUD de CrudRepository
// Solo necesitas el constructor para decirle qué clase y qué EntityManager usar
public class ServiceEntityRepositoryImpl
        extends CrudRepository<ServiceEntity, UUID>
        implements ServiceEntityRepository {

    // El constructor recibe el EntityManager (inyectado por Spring o creado manualmente)
    protected ServiceEntityRepositoryImpl(EntityManager entityManager) {
        super(ServiceEntity.class, entityManager); // Le pasas la clase y el EntityManager
    }
}

