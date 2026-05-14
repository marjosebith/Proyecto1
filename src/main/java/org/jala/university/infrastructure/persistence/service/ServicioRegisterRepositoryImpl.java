package org.jala.university.infrastructure.persistence.service;

import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ServicioRegisterRepositoryImpl {

    private static final int PARAM_NOMBRE = 1;
    private static final int PARAM_TIPO = 2;
    private static final int PARAM_PROVEEDOR = 3;
    private static final int PARAM_CATEGORIA = 4;

    private static final String SELECT_SQL =
            "SELECT service_id, service_name, service_type, provider_name, category FROM services";

    private static final String INSERT_SQL =
            "INSERT INTO services (service_name, service_type, provider_name, category) VALUES (?, ?, ?, ?)";

    public List<ServiceCatalog> findAll() {
        List<ServiceCatalog> lista = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_SQL)) {

            while (rs.next()) {
                lista.add(new ServiceCatalog(
                        String.valueOf(rs.getInt("service_id")),
                        rs.getString("service_name"),
                        rs.getString("service_type"),
                        rs.getString("provider_name"),
                        rs.getString("category")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener servicios", e);
        }

        return lista;
    }

    public void save(ServiceCatalog servicio) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {

            stmt.setString(PARAM_NOMBRE, servicio.getNombre());
            stmt.setString(PARAM_TIPO, servicio.getTipo());
            stmt.setString(PARAM_PROVEEDOR, servicio.getProveedor());
            stmt.setString(PARAM_CATEGORIA, servicio.getCategoria());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar servicio", e);
        }
    }

    public boolean existeServicio(String nombre, String proveedor) {
        String sql = "SELECT COUNT(*) FROM services WHERE LOWER(service_name) = LOWER(?) "
                + "AND LOWER(provider_name) = LOWER(?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre.trim());
            stmt.setString(2, proveedor.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia del servicio", e);
        }
        return false;
    }
}
