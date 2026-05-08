package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.service.DeleteService;
import org.jala.university.application.dto.ServiceDTO;
import org.jala.university.commons.presentation.BaseController;

import java.util.List;

public class DeleteServiceController extends BaseController {


    @FXML
    private TableColumn<ServiceDTO, String> providerColumn;
    @FXML
    private TableView<ServiceDTO> servicesTable;
    @FXML
    private TableColumn<ServiceDTO, Long> idColumn;
    @FXML
    private TableColumn<ServiceDTO, String> nameColumn;
    @FXML
    private TableColumn<ServiceDTO, String> typeColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Label statusLabel;

    private final DeleteService serviceService = new DeleteService();

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        providerColumn.setCellValueFactory(new PropertyValueFactory<>("providerName"));

        refreshButton.setOnAction(e -> cargarServicios());
        deleteButton.setOnAction(e -> eliminarServicio());

        cargarServicios();
    }

    @FXML
    private void cargarServicios() {
        List<ServiceDTO> servicios = serviceService.obtenerMisServicios();
        servicesTable.setItems(FXCollections.observableArrayList(servicios));
        statusLabel.setText("Servicios: " + servicios.size());
    }

    @FXML
    private void eliminarServicio() {
        ServiceDTO selected = servicesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona un servicio");
            alert.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Eliminar: " + selected.getServiceName() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            serviceService.eliminarServicio(selected.getServiceId());
            cargarServicios();
            statusLabel.setText("Eliminado!");
        }
    }
}
