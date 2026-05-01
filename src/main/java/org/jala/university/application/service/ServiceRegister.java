package org.jala.university.application.service;

import org.jala.university.domain.entity.Servicio;
import org.jala.university.infrastructure.persistance.service.ServicioRegisterRepositoryImpl;

import java.util.List;

public class ServiceRegister {
    private final ServicioRegisterRepositoryImpl repository = new ServicioRegisterRepositoryImpl();

    public final List<Servicio> obtenerServicios() {
        return repository.findAll();
    }

    public final void registrarServicio(String nombre, String tipo,
                                  String proveedor, String categoria) {

        Servicio servicio = new Servicio(null, nombre, tipo, proveedor, categoria);
        repository.save(servicio);
    }
}
