package org.jala.university.application.service;

import org.jala.university.application.dto.ServiceEntityDto;
import org.jala.university.application.mapper.ServiceEntityMapper;
import org.jala.university.domain.entity.ServiceEntity;
import org.jala.university.domain.repository.ServiceEntityRepository;

public final class ServiceRegistrationServiceImpl
        implements ServiceRegistrationService {

    // Dependencias necesarias — se pasan por constructor (no new adentro)
    private final ServiceEntityRepository serviceEntityRepository;
    private final ServiceEntityMapper serviceEntityMapper;

    // Constructor: Spring o el código que cree este servicio pasa las dependencias
    public ServiceRegistrationServiceImpl(
            ServiceEntityRepository serviceEntityRepository,
            ServiceEntityMapper serviceEntityMapper) {
        this.serviceEntityRepository = serviceEntityRepository;
        this.serviceEntityMapper = serviceEntityMapper;
    }

    @Override
    public ServiceEntityDto registerService(ServiceEntityDto serviceEntityDto) {

        // Paso 1: Valida que el nombre no sea nulo o vacío (criterio 5: confirmar registro)
        if (serviceEntityDto.getName() == null || serviceEntityDto.getName().isBlank()) {
            throw new IllegalArgumentException("Service name must not be empty");
        }

        // Paso 2: Valida que el userId esté presente (criterios 2 y 3: asociar al usuario)
        if (serviceEntityDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Paso 3: Convierte DTO a entidad para poder guardar en BD
        ServiceEntity entityToSave = serviceEntityMapper.mapFrom(serviceEntityDto);

        // Paso 4: Guarda en BD — save() viene de CrudRepository del commons
        ServiceEntity savedEntity = serviceEntityRepository.save(entityToSave);

        // Paso 5: Convierte la entidad guardada (ya con ID) de vuelta a DTO y lo devuelve
        // El DTO devuelto tendrá el UUID generado — la UI puede mostrar "registro exitoso"
        return serviceEntityMapper.mapTo(savedEntity);
    }
}
