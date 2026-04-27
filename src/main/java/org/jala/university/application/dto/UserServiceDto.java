package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de servicios registrados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceDto {

    private int    userServiceId;
    private int    userId;
    private int    serviceId;
    private String accountNumber;
    private String alias;
    private String notes;
    private int    isActive;          // 1 = activo, 0 = inactivo

    // ── services (solo lectura, para mostrar info) ─
    private String serviceName;
    private String serviceType;
    private String providerName;
    private String category;

    @Override
    public String toString() {
        return alias != null && !alias.isBlank() ? alias : serviceName;
    }
}

