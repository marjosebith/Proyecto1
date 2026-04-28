package org.jala.university.application.mapper;

import org.jala.university.application.dto.ServiceEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.ServiceEntity;

public final class ServiceEntityMapper
        implements Mapper<ServiceEntity, ServiceEntityDto> {

    @Override
    public ServiceEntityDto mapTo(final ServiceEntity entity) {
        return ServiceEntityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .invoiceNumber(entity.getInvoiceNumber())
                .userId(entity.getUserId())
                .build();
    }

    @Override
    public ServiceEntity mapFrom(final ServiceEntityDto dto) {
        return ServiceEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .invoiceNumber(dto.getInvoiceNumber())
                .userId(dto.getUserId())
                .build();
    }
}

