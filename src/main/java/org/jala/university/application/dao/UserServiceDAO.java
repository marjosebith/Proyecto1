package org.jala.university.application.dao;

import org.jala.university.application.dto.UserServiceDto;
import org.jala.university.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO para operaciones sobre user_services + services (SQLite + JDBC).
 */
public class UserServiceDAO {

    // ──────────────────────────────────────────────────────────────────────────
    // QUERIES
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Lista todos los servicios registrados por un usuario.
     * JOIN con services para traer nombre, tipo, proveedor y categoría.
     *
     * @param userId ID del usuario en sesión
     */
    private static final String SQL_LIST_BY_USER = """
            SELECT
                us.user_service_id,
                us.user_id,
                us.service_id,
                us.account_number,
                us.alias,
                us.notes,
                us.is_active,
                s.service_name,
                s.service_type,
                s.provider_name,
                s.category
            FROM user_services us
            INNER JOIN services s ON us.service_id = s.service_id
            WHERE us.user_id = ?
            ORDER BY us.created_at DESC
            """;

    /**
     * Busca un user_service específico por su ID primario.
     */
    private static final String SQL_FIND_BY_ID = """
            SELECT
                us.user_service_id,
                us.user_id,
                us.service_id,
                us.account_number,
                us.alias,
                us.notes,
                us.is_active,
                s.service_name,
                s.service_type,
                s.provider_name,
                s.category
            FROM user_services us
            INNER JOIN services s ON us.service_id = s.service_id
            WHERE us.user_service_id = ?
            """;

    /**
     * Actualiza los campos editables de un user_service:
     * alias, account_number, notes e is_active.
     * updated_at se renueva con CURRENT_TIMESTAMP.
     */
    private static final String SQL_UPDATE = """
            UPDATE user_services
            SET
                alias          = ?,
                account_number = ?,
                notes          = ?,
                is_active      = ?,
                updated_at     = CURRENT_TIMESTAMP
            WHERE user_service_id = ?
              AND user_id         = ?
            """;

    // ──────────────────────────────────────────────────────────────────────────
    // MAPPER privado
    // ──────────────────────────────────────────────────────────────────────────

    private UserServiceDto mapRow(ResultSet rs) throws SQLException {
        UserServiceDto dto = new UserServiceDto();
        dto.setUserServiceId(rs.getInt("user_service_id"));
        dto.setUserId(rs.getInt("user_id"));
        dto.setServiceId(rs.getInt("service_id"));
        dto.setAccountNumber(rs.getString("account_number"));
        dto.setAlias(rs.getString("alias"));
        dto.setNotes(rs.getString("notes"));
        dto.setIsActive(rs.getInt("is_active"));
        dto.setServiceName(rs.getString("service_name"));
        dto.setServiceType(rs.getString("service_type"));
        dto.setProviderName(rs.getString("provider_name"));
        dto.setCategory(rs.getString("category"));
        return dto;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // MÉTODOS PÚBLICOS
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Devuelve todos los servicios del usuario.
     *
     * @param userId ID del usuario en sesión
     * @return lista (vacía si no tiene ninguno)
     */
    public List<UserServiceDto> findAllByUser(int userId) {
        List<UserServiceDto> result = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LIST_BY_USER)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar servicios del usuario: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Busca un user_service por su ID.
     *
     * @param userServiceId PK de user_services
     * @return Optional con el DTO, o vacío si no existe
     */
    public Optional<UserServiceDto> findById(int userServiceId) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, userServiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar user_service ID=" + userServiceId + ": " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Actualiza alias, account_number, notes e is_active.
     *
     * @param dto datos editados (debe tener userServiceId y userId)
     * @return true si se modificó al menos 1 fila
     */
    public boolean update(UserServiceDto dto) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, dto.getAlias());
            ps.setString(2, dto.getAccountNumber());
            ps.setString(3, dto.getNotes());
            ps.setInt(4, dto.getIsActive());
            ps.setInt(5, dto.getUserServiceId());
            ps.setInt(6, dto.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar user_service: " + e.getMessage(), e);
        }
    }
}