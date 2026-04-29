package org.jala.university.application.service;

import org.jala.university.domain.entity.Servicio;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ExternalPaymentServiceImpl implements ExternalPaymentService {

    @Override
    public List<Servicio> buscarServicios(String texto, String campo) {

        List<Servicio> lista = new ArrayList<>();

        String sql;

        boolean buscarTodos = texto == null || texto.isBlank();

        if (buscarTodos) {
            sql = "SELECT * FROM services";
        } else {

            switch (campo.toLowerCase()) {

                case "nombre":
                    sql = "SELECT * FROM services WHERE LOWER(service_name) LIKE LOWER(?)";
                    break;

                case "tipo":
                    sql = "SELECT * FROM services WHERE LOWER(service_type) LIKE LOWER(?)";
                    break;

                case "proveedor":
                    sql = "SELECT * FROM services WHERE LOWER(provider_name) LIKE LOWER(?)";
                    break;

                case "categoria":
                    sql = "SELECT * FROM services WHERE LOWER(category) LIKE LOWER(?)";
                    break;

                default: // TODOS
                    sql = "SELECT * FROM services WHERE "
                            + "LOWER(service_name) LIKE LOWER(?) OR "
                            + "LOWER(service_type) LIKE LOWER(?) OR "
                            + "LOWER(provider_name) LIKE LOWER(?) OR "
                            + "LOWER(category) LIKE LOWER(?)";
            }
        }
        final int paramNombre = 1;
        final int paramTipo = 2;
        final int paramProveedor = 3;
        final int paramCategoria = 4;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!buscarTodos) {
                String valor = "%" + texto + "%";

                if (campo.equalsIgnoreCase("Todos")) {
                    ps.setString(paramNombre, valor);
                    ps.setString(paramTipo, valor);
                    ps.setString(paramProveedor, valor);
                    ps.setString(paramCategoria, valor);
                } else {
                    ps.setString(paramNombre, valor);
                }
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Servicio(
                        rs.getString("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_type"),
                        rs.getString("provider_name"),
                        rs.getString("category"),
                        rs.getString("description")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Servicio> listarServicios() {

        String sql = "SELECT * FROM services";
        List<Servicio> lista = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Servicio(
                        rs.getString("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_type"),
                        rs.getString("provider_name"),
                        rs.getString("category"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
