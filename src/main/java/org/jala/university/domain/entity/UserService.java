package org.jala.university.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un servicio registrado por un usuario.
 * Corresponde a la tabla user_services de la BD.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserService {

    private Long userServiceId;
    private Long userId;
    private Long serviceId;
    private String accountNumber;
    private String alias;
    private String notes;
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String serviceName;
    private String serviceType;
    private String providerName;
}