package org.jala.university;

import org.jala.university.application.service.ServiceService;
import org.jala.university.application.dto.ServiceDTO;
import java.util.List;

public final class MainTest {
    private static final int LINE_WIDTH = 80;

    private MainTest() {
    }
    public static void main(String[] args) {
        System.out.println(" === PRUEBA SERVICIOS ===\n");

        ServiceService serviceService = new ServiceService();
        List<ServiceDTO> servicios = serviceService.obtenerMisServicios();

        System.out.println(" SERVICIOS REGISTRADOS:");
        System.out.println("=".repeat(LINE_WIDTH));

        if (servicios.isEmpty()) {
            System.out.println("No hay servicios activos");
        } else {
            servicios.forEach(s ->
                    System.out.printf("| ID: %-3d | %-25s | %-12s | %-20s | %s%n",
                            s.getServiceId(),
                            s.getServiceName(),
                            s.getServiceType(),
                            s.getProviderName(),
                            s.getCategory() != null ? s.getCategory() : "")
            );
        }
        System.out.println("=".repeat(LINE_WIDTH));
    }
}
