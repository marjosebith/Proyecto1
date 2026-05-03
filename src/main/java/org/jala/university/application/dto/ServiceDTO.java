package org.jala.university.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public final class ServiceDTO {
    // Getters y Setters
    private Long serviceId;
    private String serviceName;
    private String serviceType;
    private String providerName;
    private String category;
    private LocalDateTime createdAt;

    public ServiceDTO() { }

    public ServiceDTO(Long serviceId, String serviceName, String serviceType,
                      String providerName, String category, LocalDateTime createdAt) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.providerName = providerName;
        this.category = category;
        this.createdAt = createdAt;
    }

}
