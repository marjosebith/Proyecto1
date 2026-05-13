package org.jala.university.domain.repository;

import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.domain.entity.UserService;

import java.util.List;

public interface ServiceRepository {

    List<ServiceCatalog> buscarServicios(String texto, String campo);
    List<UserService> listarServiciosPorUsuario(int userId);
}
