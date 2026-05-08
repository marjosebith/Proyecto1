package org.jala.university.application.service;

import org.jala.university.domain.entity.UserService;
import org.jala.university.domain.repository.UserServiceRepository;

import java.util.List;

public final class UpdateService {

    private final UserServiceRepository repository;

    public UpdateService(UserServiceRepository repository) {
        this.repository = repository;
    }

    public List<UserService> getServicesByUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID inválido");
        }

        return repository.findByUserId(userId);
    }

    public void update(UserService service) {

        if (service.getAlias() == null || service.getAlias().isEmpty()) {
            throw new IllegalArgumentException("Alias requerido");
        }

        repository.updateUserService(service);
    }
}
