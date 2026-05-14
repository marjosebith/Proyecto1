package org.jala.university.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class UserService {

    private Long userServiceId;
    private Long userId;
    private Long serviceId;

    private String accountNumber;
    private String alias;
    private String notes;

    // datos del catálogo
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

    @Override
    public String toString() {
        return (alias != null && !alias.isBlank())
                ? String.format("%s - %s", alias, serviceName)
                : serviceName;
    }
}
