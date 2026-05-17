package org.jala.university.domain.repository;

import org.jala.university.domain.entity.ServicePaymentDetail;
import org.jala.university.domain.entity.ServiceCatalog;

import java.util.List;

public interface ServiceRepository {

    List<ServiceCatalog> buscarServicios(String texto, String campo);
    /**
     * Retrieves payment details for a service and contract.
     *
     * @param serviceId the service identifier
     * @param numeroContrato the contract/account number
     * @return payment detail if found, otherwise null
     */
    ServicePaymentDetail obtenerDetallePago(
            Long serviceId,
            String numeroContrato
    );
}
