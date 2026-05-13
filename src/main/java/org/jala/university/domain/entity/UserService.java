package org.jala.university.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UserService {

    private Long userServiceId;
    private Long userId;
    private Long serviceId;

    private String accountNumber;
    private String alias;
    private String notes;

    private Integer isActive;

    private String serviceName;
    private String serviceType;
    private String providerName;
    private String category;

    public UserService(String userServiceId, String serviceName, String alias, String accountNumber, String notes) {
        this.userServiceId = Long.valueOf(userServiceId);
        this.serviceName = serviceName;
        this.alias = alias;
        this.accountNumber = accountNumber;
        this.notes = notes;
    }

    public UserService() { }

    public boolean isActive() {
        return isActive != null && isActive == 1;
    }

    @Override
    public String toString() {
        return alias != null && !alias.isBlank() ? alias : serviceName;
    }
}
