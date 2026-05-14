package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.service.DeleteService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.entity.UserService;
import org.jala.university.presentation.ExternalPaymentView;

import java.util.List;

public class DeleteServiceController extends BaseController {


    @FXML
    private TableColumn<UserService, String> providerColumn;
    @FXML
    private TableView<UserService> servicesTable;
    @FXML
    private TableColumn<UserService, Long> idColumn;
    @FXML
    private TableColumn<UserService, String> nameColumn;
    @FXML
    private TableColumn<UserService, String> typeColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Label statusLabel;

    private final DeleteService serviceService =
            new DeleteService();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("userServiceId")
        );
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        providerColumn.setCellValueFactory(new PropertyValueFactory<>("providerName"));

        refreshButton.setOnAction(e -> cargarServicios());
        deleteButton.setOnAction(e -> eliminarServicio());

        cargarServicios();
    }

    @FXML
    private void cargarServicios() {
        List<UserService> servicios =
                serviceService.obtenerMisServicios(1);
        //id de usuario temporal fijo debido a que no existe autenticación implementada
        servicesTable.setItems(FXCollections.observableArrayList(servicios));
        statusLabel.setText("Servicios: " + servicios.size());
    }

    @FXML
    private void eliminarServicio() {
        UserService selected =
                servicesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un servicio");
            alert.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Eliminar: " + selected.getServiceName() + "?");
        Long userId = 1L;

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            serviceService.eliminarServicio(
                    selected.getUserServiceId(),
                    userId
            );
            cargarServicios();
            statusLabel.setText("Eliminado!");
        }
    }

    @FXML private void goBack() {
        ViewSwitcher.switchTo(ExternalPaymentView.MAIN.getView());
    }
}
