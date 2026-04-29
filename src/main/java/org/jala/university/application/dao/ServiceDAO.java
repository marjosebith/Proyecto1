package org.jala.university.application.dao;

import org.jala.university.application.model.Service;
import org.jala.university.db.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ServiceDAO {


    //Lista servicios activos (ordenados por fecha creación DESC)
    public List<Service> obtenerServiciosActivos() {
        List<Service> servicios = new ArrayList<>();

        String sql = """
            SELECT service_id, service_name, service_type, provider_name, category,
            created_at, updated_at, is_active
            FROM services
            WHERE is_active = 1
            ORDER BY created_at DESC
            """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Service service = mapResultSetToService(rs);
                servicios.add(service);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener servicios: " + e.getMessage());
            e.printStackTrace();
        }
        return servicios;
    }


    // Buscar servicio por ID
    public Service obtenerServicioPorId(Long serviceId) {
        String sql = """
            SELECT service_id, service_name, service_type, provider_name, category,
                   created_at, updated_at, is_active
            FROM services
            WHERE service_id = ? AND is_active = 1
            """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, serviceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener servicio por ID: " + e.getMessage());
        }
        return null;
    }


    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getLong("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setServiceType(rs.getString("service_type"));
        service.setProviderName(rs.getString("provider_name"));
        service.setCategory(rs.getString("category"));
        service.setActive(rs.getBoolean("is_active"));
        service.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        service.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return service;
    }

    public boolean verificarTablaServices() {
        String sqlCheck = "SELECT name FROM sqlite_master WHERE type='table' AND name='services'";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {

            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error verificando tabla services: " + e.getMessage());
            return false;
        }
    }


    //Conteo de servicios activos
    public int contarServiciosActivos() {
        String sql = "SELECT COUNT(*) FROM services WHERE is_active = 1";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error contando servicios: " + e.getMessage());
        }
        return 0;
    }

    public boolean eliminarServicio(Long serviceId) {
        String sql = "DELETE FROM services WHERE service_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, serviceId);
            int filas = pstmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error eliminando servicio: " + e.getMessage());
            return false;
        }
    }
    public boolean existeServicio(Long id) {
        String sql = "SELECT 1 FROM services WHERE service_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }


}

