package org.jala.university.domain.repository;

import org.jala.university.domain.entity.ServiceCatalog;

import java.util.List;

public interface ServiceRepository {

    List<ServiceCatalog> buscarServicios(String texto, String campo);
}
