package org.jala.university.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.ServiceEntity;
import org.jala.university.domain.repository.ServiceEntityRepository;

public final class ServiceEntityRepositoryImpl
        extends CrudRepository<ServiceEntity, Integer>
        implements ServiceEntityRepository {

    protected ServiceEntityRepositoryImpl(final EntityManager entityManager) {
        super(ServiceEntity.class, entityManager);
    }

    @Override
    public Optional<ServiceEntity> findByName(final String name) {
        return getEntityManager()
                .createQuery(
                        "SELECT s FROM ServiceEntity s WHERE s.name = :name",
                        ServiceEntity.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }
}


