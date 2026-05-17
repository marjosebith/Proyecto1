package org.jala.university.application.service;

import org.jala.university.domain.entity.ServicePaymentDetail;
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


//    /**
//     * Returns the list of services associated with the given user.
//     *
//     * <p>This method is not designed for extension. Subclasses must not override it.
//     * If custom filtering is needed, compose this service rather than extending it.
//     *
//     * @param userId the ID of the user whose services will be retrieved
//     * @return a non-null list of {@link ServiceCatalog}; empty if no services found
//     */
//    @Override
//    public final List<UserService> listarServiciosPorUsuario(int userId) {
//
//        List<UserService> lista = new ArrayList<>();
//
//        String sql = """
//            SELECT
//                us.user_service_id,
//                s.service_name,
//                us.alias,
//                us.account_number,
//                us.notes
//            FROM user_services us
//            JOIN services s ON us.service_id = s.service_id
//            WHERE us.user_id = ?
//        """;
//
//        try (Connection conn = ConnectionManager.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, userId);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                lista.add(new UserService(
//                        rs.getString("user_service_id"),
//                        rs.getString("service_name"),
//                        rs.getString("alias"),
//                        rs.getString("account_number"),
//                        rs.getString("notes")
//                ));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return lista;
//    }

    /**
     * Retrieves payment details for a given service and contract.
     *
     * @param serviceId the service identifier
     * @param numeroContrato the contract/account number
     * @return a {@link ServicePaymentDetail} object with the payment details,
     *         or {@code null} if no matching record is found
     */
    @Override
    public final ServicePaymentDetail obtenerDetallePago(
            Long serviceId,
            String numeroContrato
    ) {

        String sql = """
        SELECT
            s.service_name,
            s.service_type,
            s.provider_name,
            s.category,

            u.full_name,

            us.account_number,

            COALESCE(d.amount_due, 0) AS amount_due,

            COALESCE(d.status, 'SIN DEUDA') AS status,

            COALESCE(d.due_date, '-') AS due_date

        FROM user_services us

        JOIN services s
            ON us.service_id = s.service_id

        JOIN users u
            ON us.user_id = u.user_id

        LEFT JOIN service_debts d
            ON us.user_service_id = d.user_service_id

        WHERE us.service_id = ?
        AND us.account_number = ?
        """;

        try (
                Connection conn =
                        ConnectionManager.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setLong(1, serviceId);

            ps.setString(2, numeroContrato);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new ServicePaymentDetail.Builder()

                        .serviceName(
                                rs.getString("service_name")
                        )

                        .tipo(
                                rs.getString("service_type")
                        )

                        .proveedor(
                                rs.getString("provider_name")
                        )

                        .categoria(
                                rs.getString("category")
                        )

                        .accountNumber(
                                rs.getString("account_number")
                        )

                        .usuario(
                                rs.getString("full_name")
                        )

                        .monto(
                                rs.getDouble("amount_due")
                        )

                        .estado(
                                rs.getString("status")
                        )

                        .fechaVencimiento(
                                rs.getString("due_date")
                        )

                        .build();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}
