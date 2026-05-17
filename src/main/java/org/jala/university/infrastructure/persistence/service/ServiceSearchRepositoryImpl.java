package org.jala.university.infrastructure.persistence.service;

import org.jala.university.domain.entity.Service;
import org.jala.university.infrastructure.config.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository implementation for service search operations.
 */
public final class ServiceSearchRepositoryImpl {

    /**
     * Retrieves all active services ordered by creation date descending.
     *
     * @return list of active services
     */
    public List<Service> obtenerServiciosActivos() {

        List<Service> servicios = new ArrayList<>();

        String sql = """
                SELECT
                    service_id,
                    service_name,
                    service_type,
                    provider_name,
                    category,
                    created_at,
                    updated_at
                FROM services
                ORDER BY created_at DESC
                """;

        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {

            while (rs.next()) {

                Service service =
                        mapResultSetToService(rs);

                servicios.add(service);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener servicios: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return servicios;
    }

    /**
     * Retrieves a service by its identifier.
     *
     * @param serviceId the service identifier
     * @return the service if found, otherwise null
     */
    public Service obtenerServicioPorId(Long serviceId) {

        String sql = """
                SELECT
                    service_id,
                    service_name,
                    service_type,
                    provider_name,
                    category,
                    created_at,
                    updated_at
                FROM services
                WHERE service_id = ?
                """;

        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            pstmt.setLong(1, serviceId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    return mapResultSetToService(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener servicio por ID: "
                            + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Maps a ResultSet row into a Service entity.
     *
     * @param rs database result set
     * @return mapped service entity
     * @throws SQLException if a database access error occurs
     */
    private Service mapResultSetToService(
            ResultSet rs
    ) throws SQLException {

        Service service = new Service();

        service.setServiceId(
                rs.getLong("service_id")
        );

        service.setServiceName(
                rs.getString("service_name")
        );

        service.setServiceType(
                rs.getString("service_type")
        );

        service.setProviderName(
                rs.getString("provider_name")
        );

        service.setCategory(
                rs.getString("category")
        );

        service.setCreatedAt(
                rs.getTimestamp("created_at")
                        .toLocalDateTime()
        );

        service.setUpdatedAt(
                rs.getTimestamp("updated_at")
                        .toLocalDateTime()
        );

        return service;
    }

    /**
     * Verifies whether the services table exists.
     *
     * @return true if the table exists
     */
    public boolean verificarTablaServices() {

        String sqlCheck =
                "SELECT name FROM sqlite_master "
                        + "WHERE type='table' "
                        + "AND name='services'";

        try (
                Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheck)
        ) {

            return rs.next();

        } catch (SQLException e) {

            System.err.println(
                    "Error verificando tabla services: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Counts active services.
     *
     * @return total active services
     */
    public int contarServiciosActivos() {

        String sql = "SELECT COUNT(*) FROM services";

        try (
                Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error contando servicios: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    /**
     * Deletes a service by its identifier.
     *
     * @param serviceId service identifier
     * @return true if deleted successfully
     */
    public boolean eliminarServicio(Long serviceId) {

        String sql =
                "DELETE FROM services WHERE service_id = ?";

        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            pstmt.setLong(1, serviceId);

            int filas = pstmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error eliminando servicio: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Checks whether a service exists.
     *
     * @param id service identifier
     * @return true if service exists
     */
    public boolean existeServicio(Long id) {

        String sql =
                "SELECT 1 FROM services WHERE service_id = ?";

        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            pstmt.setLong(1, id);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            return false;
        }
    }
}
