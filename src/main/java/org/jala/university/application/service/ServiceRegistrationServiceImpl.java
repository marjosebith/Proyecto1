package org.jala.university.application.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jala.university.application.dto.ServiceEntityDto;
import org.jala.university.application.mapper.ServiceEntityMapper;
import org.jala.university.domain.entity.ServiceEntity;
import org.jala.university.domain.repository.ServiceEntityRepository;

public final class ServiceRegistrationServiceImpl
        implements ServiceRegistrationService {

    private final ServiceEntityRepository serviceEntityRepository;
    private final ServiceEntityMapper serviceEntityMapper;

    public ServiceRegistrationServiceImpl(
            final ServiceEntityRepository serviceEntityRepository,
            final ServiceEntityMapper serviceEntityMapper) {
        this.serviceEntityRepository = serviceEntityRepository;
        this.serviceEntityMapper = serviceEntityMapper;
    }

    @Override
    public ServiceEntityDto registerService(final ServiceEntityDto serviceEntityDto) {
        validateFields(serviceEntityDto);
        checkServiceDoesNotExist(serviceEntityDto.getName());
        ServiceEntity entityToSave = serviceEntityMapper.mapFrom(serviceEntityDto);
        ServiceEntity savedEntity = serviceEntityRepository.save(entityToSave);
        return serviceEntityMapper.mapTo(savedEntity);
    }

    @Override
    public List<ServiceEntityDto> findAll() {
        return serviceEntityRepository.findAll()
                .stream()
                .map(serviceEntityMapper::mapTo)
                .collect(Collectors.toList());
    }

    private void validateFields(final ServiceEntityDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Service name must not be empty");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Service description must not be empty");
        }
        if (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().isBlank()) {
            throw new IllegalArgumentException("Invoice number must not be empty");
        }
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
    }

    private void checkServiceDoesNotExist(final String name) {
        serviceEntityRepository.findByName(name).ifPresent(existing -> {
            throw new IllegalArgumentException("Service already exists: " + name);
        });
    }
}
