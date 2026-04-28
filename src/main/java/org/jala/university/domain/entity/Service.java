package org.jala.university.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Service {

    private Long userServiceId;
    private Long userId;
    private Long serviceId;

    private String accountNumber;
    private String alias;
    private String notes;

    private Integer isActive;

    // datos del catálogo
    private String serviceName;
    private String serviceType;
    private String providerName;
    private String category;

    public boolean isActive() {
        return isActive != null && isActive == 1;
    }
}
