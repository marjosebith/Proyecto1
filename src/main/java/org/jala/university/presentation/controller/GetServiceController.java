package org.jala.university.presentation.controller;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jala.university.application.service.GetService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.domain.entity.UserService;

import java.util.List;


public final class GetServiceController extends BaseController {

    @FXML private TableView<UserService> servicesTable;

    // Columnas de Identificación
    @FXML private TableColumn<UserService, Long> idColumn;
    @FXML private TableColumn<UserService, String> aliasColumn;
    @FXML private TableColumn<UserService, String> accountColumn;

    // Columnas de Información Global
    @FXML private TableColumn<UserService, String> nameColumn;
    @FXML private TableColumn<UserService, String> typeColumn;
    @FXML private TableColumn<UserService, String> providerColumn;

    @FXML private Button refreshButton;

    private final GetService serviceService = new GetService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("userServiceId"));
        aliasColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        providerColumn.setCellValueFactory(new PropertyValueFactory<>("providerName"));


        refreshButton.setOnAction(e -> loadServices());


        loadServices();
    }

    @FXML
    private void loadServices() {
        try {

            List<UserService> services = serviceService.obtenerMisServicios(1);
            servicesTable.setItems(FXCollections.observableArrayList(services));
        } catch (Exception e) {
            System.err.println("Error al cargar servicios: " + e.getMessage());
        }
    }
}
