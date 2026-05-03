package org.jala.university.application.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public final class Service {
    private Long serviceId;
    private String serviceName;
    private String serviceType;
    private String providerName;
    private String category;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Service(String serviceName, String serviceType, String providerName, String category) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.providerName = providerName;
        this.category = category;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Service() { }
}

