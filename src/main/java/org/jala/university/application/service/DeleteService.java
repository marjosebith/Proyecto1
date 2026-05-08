package org.jala.university.application.service;

import org.jala.university.infrastructure.persistence.service.ServiceSearchRepositoryImpl;
import org.jala.university.application.dto.ServiceDTO;
import org.jala.university.application.mapper.ServiceMapper;
import org.jala.university.domain.entity.Service;

import java.util.List;

public class DeleteService {
    private final ServiceSearchRepositoryImpl serviceDAO;

    public DeleteService() {
        this.serviceDAO = new ServiceSearchRepositoryImpl();

        // Verificar conexión y estructura
        if (!serviceDAO.verificarTablaServices()) {
            throw new RuntimeException("Tabla 'services' no encontrada en db.sqlite3");
        }
        System.out.println("ServiceService inicializado correctamente");
    }


    //Obtener lista de servicios para mostrar en UI

    public final List<ServiceDTO> obtenerMisServicios() {
        try {
            List<Service> services = serviceDAO.obtenerServiciosActivos();
            List<ServiceDTO> dtos = ServiceMapper.toDTOList(services);
            System.out.printf("Cargados %d servicios activos%n", dtos.size());
            return dtos;
        } catch (Exception e) {
            System.err.println("Error en obtenerMisServicios: " + e.getMessage());
            return List.of(); // Lista vacía en caso de error
        }
    }


    // Buscar servicio específico

    public final ServiceDTO obtenerServicio(Long serviceId) {
        Service service = serviceDAO.obtenerServicioPorId(serviceId);
        return ServiceMapper.toDTO(service);
    }


    public final int getTotalServiciosActivos() {
        return serviceDAO.contarServiciosActivos();
    }

    public final void eliminarServicio(Long serviceId) {
        if (serviceId == null || serviceId <= 0) {
            return;
        }

        boolean eliminado = serviceDAO.eliminarServicio(serviceId);

        if (eliminado) {
            System.out.println("Servicio eliminado PERMANENTEMENTE");
        } else {
            System.out.println("No se encontró el servicio");
        }

    }
}
