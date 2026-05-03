package org.jala.university.presentation.controller;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.service.ServiceEliminar;
import org.jala.university.application.dto.ServiceDTO;
import java.util.List;

public class ServicesController {

    @FXML private TableView<ServiceDTO> servicesTable;
    @FXML private TableColumn<ServiceDTO, Long> idColumn;
    @FXML private TableColumn<ServiceDTO, String> nameColumn;
    @FXML private TableColumn<ServiceDTO, String> typeColumn;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;
    @FXML private Label statusLabel;

    private final ServiceEliminar serviceService = new ServiceEliminar();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));

        refreshButton.setOnAction(e -> cargarServicios());
        deleteButton.setOnAction(e -> eliminarServicio());

        cargarServicios();
    }

    @FXML private void cargarServicios() {
        List<ServiceDTO> servicios = serviceService.obtenerMisServicios();
        servicesTable.setItems(FXCollections.observableArrayList(servicios));
        statusLabel.setText("Servicios: " + servicios.size());
    }

    @FXML private void eliminarServicio() {
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
