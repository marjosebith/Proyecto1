package org.jala.university.infrastructure.persistence.service;

import org.jala.university.domain.entity.UserService;
import org.jala.university.domain.repository.UserServiceRepository;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ServiceRepositoryImpl implements UserServiceRepository {

    private static final int PARAM_ACCOUNT_NUMBER = 1;
    private static final int PARAM_ALIAS         = 2;
    private static final int PARAM_NOTES         = 3;
    private static final int PARAM_IS_ACTIVE     = 4;
    private static final int PARAM_USER_SERVICE_ID = 5;  // WHERE clause

    @Override
    public List<UserService> findByUserId(int userId) {

        String sql = """
            SELECT us.user_service_id, us.account_number, us.alias, us.notes,
                   us.is_active, s.service_id, s.service_name,
                   s.service_type, s.provider_name, s.category
            FROM user_services us
            JOIN services s ON us.service_id = s.service_id
            WHERE us.user_id = ? AND us.is_active = 1
        """;

        List<UserService> list = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserService s = new UserService();

                s.setUserServiceId(rs.getLong("user_service_id"));
                s.setServiceId(rs.getLong("service_id"));

                s.setAccountNumber(rs.getString("account_number"));
                s.setAlias(rs.getString("alias"));
                s.setNotes(rs.getString("notes"));

                s.setIsActive(rs.getInt("is_active"));

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
            SET account_number = ?, alias = ?, notes = ?, is_active = ?
            WHERE user_service_id = ?
        """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(PARAM_ACCOUNT_NUMBER,   service.getAccountNumber());
            stmt.setString(PARAM_ALIAS,            service.getAlias());
            stmt.setString(PARAM_NOTES,            service.getNotes());
            stmt.setInt(PARAM_IS_ACTIVE,           service.isActive() ? 1 : 0);
            stmt.setLong(PARAM_USER_SERVICE_ID,    service.getUserServiceId()); // ← usaba serviceId antes

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
