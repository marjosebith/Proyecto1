package org.jala.university.application.service;

import org.jala.university.domain.entity.Service;
import org.jala.university.domain.repository.ServiceRepository;

import java.util.List;

public final class ServiceService {

    private final ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<Service> getServicesByUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("User ID inválido");
        }

        return repository.findByUserId(userId);
    }

    public void update(Service service) {

        if (service.getAlias() == null || service.getAlias().isEmpty()) {
            throw new IllegalArgumentException("Alias requerido");
        }

        repository.updateUserService(service);
    }
}
