package org.jala.university.application.service;

import org.jala.university.application.dao.UserServiceDAO;
import org.jala.university.application.dto.UserServiceDto;

import java.util.List;
import java.util.Optional;

/**
 * Capa de negocio para la gestión de servicios registrados por el usuario.
 */
public class UserServiceService {

    private final UserServiceDAO dao = new UserServiceDAO();

    // ──────────────────────────────────────────────────────────────────────────
    // LISTAR
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Devuelve los servicios registrados por el usuario.
     *
     * @param userId ID del usuario en sesión
     */
    public List<UserServiceDto> getUserServices(int userId) {
        return dao.findAllByUser(userId);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // OBTENER UNO
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Carga un user_service por ID para prellenar el formulario de edición.
     *
     * @throws IllegalArgumentException si no existe
     */
    public UserServiceDto getById(int userServiceId) {
        return dao.findById(userServiceId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el servicio con ID: " + userServiceId));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ACTUALIZAR
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Valida y persiste los cambios de un user_service.
     *
     * @param dto datos editados
     * @throws IllegalArgumentException si la validación falla
     */
    public void update(UserServiceDto dto) {
        validate(dto);
        boolean updated = dao.update(dto);
        if (!updated) {
            throw new RuntimeException("No se pudo actualizar. El registro no existe o no te pertenece.");
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // VALIDACIÓN
    // ──────────────────────────────────────────────────────────────────────────

    private void validate(UserServiceDto dto) {
        if (dto.getAlias() != null && dto.getAlias().length() > 100) {
            throw new IllegalArgumentException("El alias no puede superar 100 caracteres.");
        }
        if (dto.getAccountNumber() != null && dto.getAccountNumber().isBlank()) {
            // Si envían cadena vacía, la convertimos a null para no romper UNIQUE
            dto.setAccountNumber(null);
        }
        if (dto.getNotes() != null && dto.getNotes().length() > 500) {
            throw new IllegalArgumentException("Las notas no pueden superar 500 caracteres.");
        }
    }
}