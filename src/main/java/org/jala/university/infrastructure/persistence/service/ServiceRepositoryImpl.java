package org.jala.university.infrastructure.persistence.service;

import org.jala.university.domain.entity.UserService;
import org.jala.university.domain.repository.UserServiceRepository;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ServiceRepositoryImpl implements UserServiceRepository {

    private static final int PARAM_ACCOUNT_NUMBER = 1;
    private static final int PARAM_ALIAS          = 2;
    private static final int PARAM_NOTES          = 3;
    private static final int PARAM_USER_SERVICE_ID = 4;

    @Override
    public List<UserService> findByUserId(int userId) {

        String sql = """
            SELECT us.user_service_id, us.account_number, us.alias, us.notes,
                s.service_id, s.service_name,
                   s.service_type, s.provider_name, s.category
            FROM user_services us
            JOIN services s ON us.service_id = s.service_id
            WHERE us.user_id = ?
        """;

        List<UserService> list = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserService s = new UserService();


                s.setUserServiceId(
                        rs.getLong("user_service_id")
                );
                s.setUserId((long) userId);
                s.setServiceId(rs.getLong("service_id"));

                s.setAccountNumber(rs.getString("account_number"));
                s.setAlias(rs.getString("alias"));
                s.setNotes(rs.getString("notes"));

                s.setServiceName(rs.getString("service_name"));
                s.setServiceType(rs.getString("service_type"));
                s.setProviderName(rs.getString("provider_name"));
                s.setCategory(rs.getString("category"));

                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void updateUserService(UserService service) {

        String sql = """
            UPDATE user_services
            SET account_number = ?, alias = ?, notes = ?
            WHERE user_service_id = ?
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(PARAM_ACCOUNT_NUMBER,   service.getAccountNumber());
            stmt.setString(PARAM_ALIAS,            service.getAlias());
            stmt.setString(PARAM_NOTES,            service.getNotes());
            stmt.setLong(PARAM_USER_SERVICE_ID,    service.getUserServiceId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar en la base de datos", e);
        }
    }


    @Override
    public void deleteUserService(
            Long userServiceId,
            Long userId
    ) {

        String sql = """
        DELETE FROM user_services
        WHERE user_service_id = ?
        AND user_id = ?
    """;

        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, userServiceId);
            stmt.setLong(2, userId);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException(
                        "No se encontró el servicio del usuario"
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error eliminando servicio",
                    e
            );
        }
    }
}
