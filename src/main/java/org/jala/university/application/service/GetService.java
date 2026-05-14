package org.jala.university.application.service;


import org.jala.university.domain.entity.UserService;
import org.jala.university.infrastructure.persistence.service.ServiceRepositoryImpl;

import java.util.List;

public class GetService {
    private final ServiceRepositoryImpl repository;

    public GetService() {
        this.repository = new ServiceRepositoryImpl();
    }

    //Obtener lista de servicios para mostrar en UI

    public final List<UserService> obtenerMisServicios(
            int userId
    ) {

        return repository.findByUserId(userId);
    }

}
