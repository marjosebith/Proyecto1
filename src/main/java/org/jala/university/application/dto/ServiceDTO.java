package org.jala.university.application.dto;


import java.time.LocalDateTime;

public class ServiceDTO {
    private Long serviceId;
    private String serviceName;
    private String serviceType;
    private String providerName;
    private String category;
    private LocalDateTime createdAt;

    public ServiceDTO() {}

    public ServiceDTO(Long serviceId, String serviceName, String serviceType,
                      String providerName, String category, LocalDateTime createdAt) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.providerName = providerName;
        this.category = category;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
