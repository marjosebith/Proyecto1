package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import org.jala.university.application.service.ExternalPaymentService;
import org.jala.university.application.service.ExternalPaymentServiceImpl;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.domain.entity.Servicio;

import java.util.List;

public final class BuscarServicioController extends BaseController {

    @FXML
    private TextField campoBusqueda;

    @FXML
    private ComboBox<String> filtroCampo;

    @FXML
    private TableView<Servicio> tabla;

    @FXML
    private TableColumn<Servicio, String> colId;

    @FXML
    private TableColumn<Servicio, String> colNombre;

    @FXML
    private TableColumn<Servicio, String> colTipo;

    @FXML
    private TableColumn<Servicio, String> colProveedor;

    @FXML
    private TableColumn<Servicio, String> colCategoria;

    private ExternalPaymentService service;

    @FXML
    public void initialize() {

        service = new ExternalPaymentServiceImpl();

        filtroCampo.getItems().addAll("Todos", "nombre", "tipo", "proveedor", "categoria");
        filtroCampo.setValue("Todos");

        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));
        colProveedor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProveedor()));
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoria()));

        //trae todos los servicios iniciales
        //cargarDatos("", "Todos");
        tabla.getItems().clear();

        //busqueda automatica
//        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
//            onBuscarClick();
//        });

        //busqueda por enter o click
        campoBusqueda.setOnAction(e -> onBuscarClick());
    }

    @FXML
    public void onBuscarClick() {

        String texto = campoBusqueda.getText();
        String campo = filtroCampo.getValue();

        if (texto == null || texto.isBlank()) {
            cargarDatos("", "Todos");
            return;
        }

        cargarDatos(texto, campo);
    }

    private void cargarDatos(String texto, String campo) {

        List<Servicio> lista = service.buscarServicios(texto, campo);
        tabla.setItems(FXCollections.observableArrayList(lista));

        if (lista.isEmpty()) {
            tabla.setPlaceholder(new Label("No se encuentran registros con su búsqueda"));
        } else {
            tabla.setPlaceholder(new Label("")); // limpiar mensaje
        }
    }
}
