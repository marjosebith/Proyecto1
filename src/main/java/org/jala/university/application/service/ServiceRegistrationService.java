package org.jala.university.application.service;

import java.util.List;
import org.jala.university.application.dto.ServiceEntityDto;

public interface ServiceRegistrationService {

    ServiceEntityDto registerService(ServiceEntityDto serviceEntityDto);

    List<ServiceEntityDto> findAll();
}

