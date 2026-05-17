package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import org.jala.university.application.service.SearchService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.domain.repository.ServiceRepository;
import org.jala.university.presentation.ExternalPaymentView;

import java.util.List;

public final class BuscarServicioController extends BaseController {

    @FXML
    private TextField campoBusqueda;

    @FXML
    private ComboBox<String> filtroCampo;

    @FXML
    private TableView<ServiceCatalog> tabla;

    @FXML
    private TableColumn<ServiceCatalog, String> colId;

    @FXML
    private TableColumn<ServiceCatalog, String> colNombre;

    @FXML
    private TableColumn<ServiceCatalog, String> colTipo;

    @FXML
    private TableColumn<ServiceCatalog, String> colProveedor;

    @FXML
    private TableColumn<ServiceCatalog, String> colCategoria;

    private ServiceRepository service;

    @FXML
    public void initialize() {

        service = new SearchService();

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

        // Si está vacío, mostrara todos los servicios
        if (texto == null || texto.isBlank()) {
            cargarDatos("", "Todos");
            return;
        }

        final int minimoCaracteres = 3;
        // mínimo 3 caracteres para la busqueda
        if (texto.length() < minimoCaracteres) {
            tabla.getItems().clear();
            tabla.setPlaceholder(new Label("Ingrese al menos 3 caracteres"));
            return;
        }

        cargarDatos(texto, campo);
    }

    private void cargarDatos(String texto, String campo) {

        List<ServiceCatalog> lista = service.buscarServicios(texto, campo);
        tabla.setItems(FXCollections.observableArrayList(lista));

        if (lista.isEmpty()) {
            tabla.setPlaceholder(new Label("No se encuentran registros con su búsqueda"));
        } else {
            tabla.setPlaceholder(new Label("")); // limpiar mensaje
        }
    }

    @FXML
    private void onVolverClick() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.MAIN.getView()
        );
    }
}
