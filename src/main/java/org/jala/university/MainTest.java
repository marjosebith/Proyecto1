package org.jala.university;

import org.jala.university.application.service.ServiceService;
import org.jala.university.application.dto.ServiceDTO;
import java.util.List;
import java.util.Scanner;

public final class MainTest {
    private static final int LINE_WIDTH = 90;
    private MainTest() {
    }
    private static ServiceService serviceService = new ServiceService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== PRUEBA  ===\n");

        while (true) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1 -> listarServicios();
                case 2 -> eliminarServicio();
                default -> System.out.println("Opción inválida");
            }
        }
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("MENÚ PRINCIPAL:");
        System.out.println("1. Listar servicios");
        System.out.println("2. Eliminar servicio");
        System.out.println("0. Salir");
        System.out.print("Elige opción: ");
    }

    private static void listarServicios() {
        System.out.println("\n === SERVICIOS ACTIVOS ===");
        List<ServiceDTO> servicios = serviceService.obtenerMisServicios();

        if (servicios.isEmpty()) {
            System.out.println("No hay servicios registrados");
            return;
        }

        System.out.println("=".repeat(LINE_WIDTH));
        servicios.forEach(s ->
                System.out.printf("| ID: %-3d | %-25s | %-12s | %-20s | %s%n",
                        s.getServiceId(),
                        s.getServiceName(),
                        s.getServiceType(),
                        s.getProviderName(),
                        s.getCategory() != null ? s.getCategory() : "N/A")
        );
        System.out.println("=".repeat(LINE_WIDTH));
    }


    private static void eliminarServicio() {
        listarServicios();

        System.out.print("\nIngresa ID a eliminar (0=cancelar): ");
        Long id = scanner.nextLong();

        if (id == 0) {
            System.out.println("Cancelado");
            return;
        }

        System.out.print("Confirmar eliminación de ID " + id + " (s/n): ");
        String confirm = scanner.next();

        if ("s".equalsIgnoreCase(confirm)) {
            boolean eliminado = serviceService.eliminarServicio(id);
            if (eliminado) {
                System.out.println("Servicio ELIMINADO correctamente");
                listarServicios(); // Mostrar lista actualizada
            } else {
                System.out.println("Error: Servicio no encontrado o ya eliminado");
            }
        } else {
            System.out.println("Cancelado");
        }
    }

}
