package org.jala.university.application.service;

import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.infrastructure.persistence.service.ServicioRegisterRepositoryImpl;

import java.util.List;

public class CreateService {
    private final ServicioRegisterRepositoryImpl repository = new ServicioRegisterRepositoryImpl();

    public final List<ServiceCatalog> obtenerServicios() {
        return repository.findAll();
    }

    public final void registrarServicio(String nombre, String tipo, String proveedor, String categoria) {

        ServiceCatalog servicio = new ServiceCatalog(null, nombre, tipo, proveedor, categoria);
        repository.save(servicio);
    }
}
