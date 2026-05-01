package org.jala.university.presentation.controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import org.jala.university.application.service.ServiceRegister;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.domain.entity.Servicio;

public class RegistrarServicioController extends BaseController {

    @FXML private TableView<Servicio> tableServicios;
    @FXML private TableColumn<Servicio, Integer> colId;
    @FXML private TableColumn<Servicio, String> colNombre;
    @FXML private TableColumn<Servicio, String> colDescripcion;
    @FXML private TableColumn<Servicio, String> colFactura;

    @FXML private Label labelConteo;
    @FXML private Label labelMensaje;

    private final ObservableList<Servicio> servicios = FXCollections.observableArrayList();
    private final ServiceRegister service = new ServiceRegister();

    @FXML
    public final void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFactura.setCellValueFactory(new PropertyValueFactory<>("proveedor"));

        tableServicios.setItems(servicios);
        cargarServicios();
    }

    @FXML
    private void handleRegistrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Formulari.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            FormularioServicioController controller = loader.getController();
            controller.setOnRegistroExitoso(nombre -> {
                cargarServicios();
                mostrarExito(nombre);
            });

            stage.showAndWait();

        } catch (Exception e) {
            labelMensaje.setText("Error al abrir formulario");
        }
    }

    private void cargarServicios() {
        servicios.clear();
        servicios.addAll(service.obtenerServicios());
        labelConteo.setText(servicios.size() + " servicios");
    }

    private void mostrarExito(String nombre) {
        labelMensaje.setText("Servicio " + nombre + " registrado");
    }
}
