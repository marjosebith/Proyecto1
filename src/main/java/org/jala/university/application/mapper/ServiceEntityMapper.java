package org.jala.university.application.mapper;

// Mapper del commons: obliga a implementar mapTo y mapFrom
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.application.dto.ServiceEntityDto;
import org.jala.university.domain.entity.ServiceEntity;

// Traduce en ambas direcciones: ServiceEntity ↔ ServiceEntityDto
public final class ServiceEntityMapper
        implements Mapper<ServiceEntity, ServiceEntityDto> {

    // ServiceEntity → ServiceEntityDto (para enviar datos a la UI)
    @Override
    public ServiceEntityDto mapTo(ServiceEntity entity) {
        return ServiceEntityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .userId(entity.getUserId())
                .build();
    }

    // ServiceEntityDto → ServiceEntity (para guardar en BD)
    @Override
    public ServiceEntity mapFrom(ServiceEntityDto dto) {
        return ServiceEntity.builder()
                .id(dto.getId())           // null si es nuevo — JPA genera el UUID
                .name(dto.getName())
                .description(dto.getDescription())
                .userId(dto.getUserId())   // obligatorio — viene del usuario activo
                .build();
    }
}
