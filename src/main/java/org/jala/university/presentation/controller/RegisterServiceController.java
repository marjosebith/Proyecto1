package org.jala.university.presentation.controller;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import org.jala.university.application.service.CreateService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.presentation.ExternalPaymentView;

import java.net.URL;

public class RegisterServiceController extends BaseController {

    @FXML private TableView<ServiceCatalog> tableServicios;
    @FXML private TableColumn<ServiceCatalog, Integer> colId;
    @FXML private TableColumn<ServiceCatalog, String> colNombre;
    @FXML private TableColumn<ServiceCatalog, String> colDescripcion;
    @FXML private TableColumn<ServiceCatalog, String> colProveedor;

    @FXML private Label labelConteo;
    @FXML private Label labelMensaje;

    private final ObservableList<ServiceCatalog> servicios = FXCollections.observableArrayList();
    private final CreateService service = new CreateService();

    @FXML
    public final void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));

        tableServicios.setItems(servicios);
        cargarServicios();
    }

    @FXML
    private void handleRegistrar() {
        try {
            URL resource = getClass().getResource(
                    "/fxml/Formulari.fxml"
            );

            FXMLLoader loader = new FXMLLoader(resource);

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
            e.printStackTrace();
            labelMensaje.setText(e.getMessage());
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

    @FXML private void goBack() {
        ViewSwitcher.switchTo(ExternalPaymentView.MAIN.getView());
    }
}
