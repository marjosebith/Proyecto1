package org.jala.university.application.mapper;



import org.jala.university.application.dto.ServiceDTO;
import org.jala.university.application.model.Service;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceMapper {

    public static ServiceDTO toDTO(Service service) {
        if (service == null) return null;

        return new ServiceDTO(
                service.getServiceId(),
                service.getServiceName(),
                service.getServiceType(),
                service.getProviderName(),
                service.getCategory(),
                service.getCreatedAt()
        );
    }

    public static Service toEntity(ServiceDTO dto) {
        if (dto == null) return null;

        Service service = new Service();
        service.setServiceName(dto.getServiceName());
        service.setServiceType(dto.getServiceType());
        service.setProviderName(dto.getProviderName());
        service.setCategory(dto.getCategory());
        service.setCreatedAt(dto.getCreatedAt());
        return service;
    }

    public static List<ServiceDTO> toDTOList(List<Service> services) {
        return services.stream()
                .map(ServiceMapper::toDTO)
                .collect(Collectors.toList());
    }
}
