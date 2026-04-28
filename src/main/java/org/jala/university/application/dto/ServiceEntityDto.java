package org.jala.university.application.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ServiceEntityDto {

    Integer id;
    String name;
    String description;
    String invoiceNumber;
    UUID userId;
}

