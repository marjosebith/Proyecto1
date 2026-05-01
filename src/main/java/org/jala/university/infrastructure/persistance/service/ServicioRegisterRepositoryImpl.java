package org.jala.university.infrastructure.persistance.service;

import org.jala.university.domain.entity.Servicio;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioRegisterRepositoryImpl {

    private static final int PARAM_NOMBRE = 1;
    private static final int PARAM_TIPO = 2;
    private static final int PARAM_PROVEEDOR = 3;
    private static final int PARAM_CATEGORIA = 4;

    private static final String SELECT_SQL =
            "SELECT service_id, service_name, service_type, provider_name, category FROM services WHERE is_active = 1";

    private static final String INSERT_SQL =
            "INSERT INTO services (service_name, service_type, provider_name, category) VALUES (?, ?, ?, ?)";

    public final List<Servicio> findAll() {
        List<Servicio> lista = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_SQL)) {

            while (rs.next()) {
                lista.add(new Servicio(
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

    public final void save(Servicio servicio) {
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
}
