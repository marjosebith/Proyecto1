package org.jala.university.test;

import org.jala.university.application.dao.ServicesDAO;
import org.jala.university.application.validation.UserServiceValidator;
import org.jala.university.domain.entity.UserService;

import java.sql.SQLException;
import java.util.List;

/**
 * Clase de prueba para verificar la funcionalidad de edición de servicios.
 * NOTA: Ejecutar después de que la BD esté inicializada.
 */
public class TestEditService {

    public static void main(String[] args) {
        System.out.println("====== TEST: Editar Servicio ======\n");

        ServicesDAO dao = new ServicesDAO();

        try {
            // 1. Obtener servicios del usuario 1
            System.out.println("1. Cargando servicios del usuario 1...");
            List<UserService> services = dao.getUserServices(1L);
            System.out.println("   Servicios encontrados: " + services.size());

            if (!services.isEmpty()) {
                UserService service = services.get(0);
                System.out.println("   - " + service.getAlias() + " (" + service.getServiceName() + ")");

                // 2. Obtener un servicio específico
                System.out.println("\n2. Cargando servicio por ID: " + service.getUserServiceId());
                UserService singleService = dao.getUserServiceById(service.getUserServiceId());
                if (singleService != null) {
                    System.out.println("   ✓ Servicio cargado correctamente");
                    System.out.println("   - Alias: " + singleService.getAlias());
                    System.out.println("   - Servicio: " + singleService.getServiceName());
                    System.out.println("   - Proveedor: " + singleService.getProviderName());
                }

                // 3. Pruebas de validación
                System.out.println("\n3. Probando validaciones...");

                // 3a. Alias vacío
                List<String> errors = UserServiceValidator.validate("", "1234567890", "notas");
                System.out.println("   Alias vacío: " + (errors.isEmpty() ? "INCORRECTO" : "✓ Validado - " + errors.size() + " errores"));

                // 3b. Alias válido
                errors = UserServiceValidator.validate("Mi Servicio", "1234567890", "notas");
                System.out.println("   Alias válido: " + (errors.isEmpty() ? "✓ Válido" : "INCORRECTO"));

                // 3c. Número de cuenta inválido
                errors = UserServiceValidator.validate("Mi Servicio", "ABC@#$", "notas");
                System.out.println("   Número cuenta inválido: " + (errors.isEmpty() ? "INCORRECTO" : "✓ Validado - " + errors.get(0)));

                // 4. Actualizar servicio
                System.out.println("\n4. Intentando actualizar servicio...");
                singleService.setAlias("Test - " + System.currentTimeMillis());
                singleService.setNotes("Actualizado desde prueba");

                boolean updated = dao.updateUserService(singleService);
                System.out.println("   Actualización: " + (updated ? "✓ EXITOSA" : "FALLIDA"));

                if (updated) {
                    // Verificar cambio
                    UserService verificacion = dao.getUserServiceById(singleService.getUserServiceId());
                    System.out.println("   - Nuevo alias: " + verificacion.getAlias());
                    System.out.println("   - Nuevas notas: " + verificacion.getNotes());
                }
            }

            System.out.println("\n====== TEST COMPLETADO ======");

        } catch (SQLException e) {
            System.err.println("ERROR en BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

