package org.jala.university.application.service;



import org.jala.university.application.dao.ServiceDAO;
import org.jala.university.application.dto.ServiceDTO;
import org.jala.university.application.mapper.ServiceMapper;
import org.jala.university.application.model.Service;

import java.util.List;

public class ServiceService {
    private final ServiceDAO serviceDAO;
    private final ServiceMapper mapper;

    public ServiceService() {
        this.serviceDAO = new ServiceDAO();
        this.mapper = new ServiceMapper();

        // Verificar conexión y estructura
        if (!serviceDAO.verificarTablaServices()) {
            throw new RuntimeException("Tabla 'services' no encontrada en db.sqlite3");
        }
        System.out.println("ServiceService inicializado correctamente");
    }


    //Obtener lista de servicios para mostrar en UI

    public List<ServiceDTO> obtenerMisServicios() {
        try {
            List<Service> services = serviceDAO.obtenerServiciosActivos();
            List<ServiceDTO> dtos = mapper.toDTOList(services);
            System.out.printf("Cargados %d servicios activos%n", dtos.size());
            return dtos;
        } catch (Exception e) {
            System.err.println("Error en obtenerMisServicios: " + e.getMessage());
            return List.of(); // Lista vacía en caso de error
        }
    }


    // Buscar servicio específico

    public ServiceDTO obtenerServicio(Long serviceId) {
        Service service = serviceDAO.obtenerServicioPorId(serviceId);
        return mapper.toDTO(service);
    }


    public int getTotalServiciosActivos() {
        return serviceDAO.contarServiciosActivos();
    }

    public boolean eliminarServicio(Long serviceId) {
        if (serviceId == null || serviceId <= 0) {
            return false;
        }

        // Verificar existencia
        if (!serviceDAO.existeServicioActivo(serviceId)) {
            System.out.println("⚠Servicio ID=" + serviceId + " no encontrado o ya eliminado");
            return false;
        }

        boolean eliminado = serviceDAO.eliminarServicio(serviceId);
        if (eliminado) {
            System.out.println("Servicio ID=" + serviceId + " eliminado correctamente");
        }
        return eliminado;
    }
}

