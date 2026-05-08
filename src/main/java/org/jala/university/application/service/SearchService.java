package org.jala.university.application.service;

import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.domain.repository.ServiceRepository;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SearchService implements ServiceRepository {
    @Override
    public final List<ServiceCatalog> buscarServicios(String texto, String campo) {

        List<ServiceCatalog> lista = new ArrayList<>();

        String sql;

        boolean buscarTodos = texto == null || texto.isBlank();

        if (buscarTodos) {
            sql = "SELECT service_id, service_name, service_type, provider_name, category FROM services";
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
                lista.add(new ServiceCatalog(
                        rs.getString("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_type"),
                        rs.getString("provider_name"),
                        rs.getString("category")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
