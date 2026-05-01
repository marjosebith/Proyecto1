package org.jala.university.presentation.controller;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.dto.ServiceDTO;
import org.jala.university.application.service.ServiceVerService;
import java.util.List;

public class ServicesViewController {

    @FXML private TableView<ServiceDTO> servicesTable;
    @FXML private TableColumn<ServiceDTO, Long> idColumn;
    @FXML private TableColumn<ServiceDTO, String> nameColumn;
    @FXML private TableColumn<ServiceDTO, String> typeColumn;
    @FXML private TableColumn<ServiceDTO, String> providerColumn;
    @FXML private Button refreshButton;

    private final ServiceVerService serviceService = new ServiceVerService();

    @FXML
    private void initialize() {
        // Columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        providerColumn.setCellValueFactory(new PropertyValueFactory<>("providerName"));

        // Botón
        refreshButton.setOnAction(e -> loadServices());

        // Cargar
        loadServices();
    }

    @FXML
    private void loadServices() {
        List<ServiceDTO> services = serviceService.obtenerMisServicios();
        servicesTable.setItems(FXCollections.observableArrayList(services));
    }
}
