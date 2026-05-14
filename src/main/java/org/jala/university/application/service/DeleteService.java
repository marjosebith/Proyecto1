package org.jala.university.application.service;

import org.jala.university.domain.entity.UserService;
import org.jala.university.infrastructure.persistence.service.ServiceRepositoryImpl;

import java.util.List;

public final class DeleteService {

    private final ServiceRepositoryImpl repository =
            new ServiceRepositoryImpl();

    public List<UserService> obtenerMisServicios(int userId) {

        return repository.findByUserId(userId);
    }

    public void eliminarServicio(
            Long userServiceId,
            Long userId
    ) {

        if (userServiceId == null || userId == null) {
            throw new IllegalArgumentException("IDs inválidos");
        }

        repository.deleteUserService(
                userServiceId,
                userId
        );
    }
}
