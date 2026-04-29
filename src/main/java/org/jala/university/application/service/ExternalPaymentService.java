package org.jala.university.application.service;

import org.jala.university.domain.entity.Servicio;

import java.util.List;

public interface ExternalPaymentService {
    // Here should be added all the required methods that will handle business logic
    List<Servicio> buscarServicios(String texto, String campo);

    List<Servicio> listarServicios();
}
