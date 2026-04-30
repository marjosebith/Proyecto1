package org.jala.university.presentation.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.infrastructure.config.ConnectionManager;

public class MainViewController extends BaseController {

    @FXML
    private TableView<ServiceRow> tableServicios;

    @FXML
    private TableColumn<ServiceRow, Integer> colId;

    @FXML
    private TableColumn<ServiceRow, String> colNombre;

    @FXML
    private TableColumn<ServiceRow, String> colDescripcion;

    @FXML
    private TableColumn<ServiceRow, String> colFactura;

    @FXML
    private Label labelConteo;

    @FXML
    private Label labelMensaje;

    private final ObservableList<ServiceRow> servicios = FXCollections.observableArrayList();

    private static final String SELECT_SQL =
            "SELECT service_id, service_name, service_type, provider_name, category"
                    + " FROM services WHERE is_active = 1";

    @FXML
    public final void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFactura.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        tableServicios.setItems(servicios);
        labelMensaje.setVisible(false);
        labelMensaje.setManaged(false);
        cargarServicios();
    }

    @FXML
    public final void handleRegistrar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Formulari.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Registrar servicio");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            Formulari controller = loader.getController();
            controller.setOnRegistroExitoso(nombre -> {
                cargarServicios();
                mostrarExito(nombre);
            });
            stage.showAndWait();
        } catch (IOException e) {
            labelMensaje.setText("Error al abrir el formulario.");
            labelMensaje.setVisible(true);
            labelMensaje.setManaged(true);
        }
    }

    private void cargarServicios() {
        servicios.clear();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_SQL)) {
            while (rs.next()) {
                servicios.add(new ServiceRow(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_type"),
                        rs.getString("provider_name"),
                        rs.getString("category")
                ));
            }
            labelConteo.setText(servicios.size() + " servicios");
        } catch (SQLException e) {
            labelMensaje.setText("Error al cargar servicios.");
            labelMensaje.setVisible(true);
            labelMensaje.setManaged(true);
        }
    }

    private void mostrarExito(final String nombre) {
        labelMensaje.setText("El servicio " + nombre + " fue registrado correctamente.");
        labelMensaje.setVisible(true);
        labelMensaje.setManaged(true);
    }

    public static final class ServiceRow {

        private final int id;
        private final String nombre;
        private final String tipo;
        private final String proveedor;
        private final String categoria;

        public ServiceRow(final int id, final String nombre,
                          final String tipo, final String proveedor, final String categoria) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.proveedor = proveedor;
            this.categoria = categoria;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public String getProveedor() {
            return proveedor;
        }

        public String getCategoria() {
            return categoria;
        }
    }
}

