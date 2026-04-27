package org.jala.university.application.dto;

// @Value de Lombok: hace la clase inmutable (todos los campos son final)
// @Builder: permite construir con ServiceEntityDto.builder().name("...").build()
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value  // Hace la clase inmutable — importante para DTOs (no deben mutar en tránsito)
public class ServiceEntityDto {

    UUID id;           // Puede ser null cuando el servicio aún no fue guardado
    String name;       // Nombre del servicio
    String description; // Descripción del servicio
    UUID userId;       // ID del usuario al que se asocia — viene del formulario o sesión
}
