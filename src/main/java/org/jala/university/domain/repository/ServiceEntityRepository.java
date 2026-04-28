package org.jala.university.domain.repository;

import java.util.Optional;
import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.ServiceEntity;

public interface ServiceEntityRepository extends Repository<ServiceEntity, Integer> {

    Optional<ServiceEntity> findByName(String name);
}


