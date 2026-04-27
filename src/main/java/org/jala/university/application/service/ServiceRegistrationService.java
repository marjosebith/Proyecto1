package org.jala.university.application.service;

import org.jala.university.application.dto.ServiceEntityDto;

// La interfaz define el "contrato" — qué puede hacer este servicio
// El controlador de JavaFX solo conoce esta interfaz, no la implementación
// Esto facilita testing y cambios futuros sin tocar la UI
public interface ServiceRegistrationService {

    // Registra un nuevo servicio y devuelve el DTO con el ID generado
    // Si algo falla (ej: nombre vacío), lanzará una excepción
    ServiceEntityDto registerService(ServiceEntityDto serviceEntityDto);
}

