package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jala.university.application.service.SearchService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.entity.ServiceCatalog;
import org.jala.university.domain.entity.ServicePaymentDetail;
import org.jala.university.presentation.ExternalPaymentView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public final class ServicePaymentDetailsController extends BaseController {

    @FXML
    private ComboBox<ServiceCatalog> cmbServicios;

    @FXML
    private TextField txtContrato;

    @FXML
    private Label lblServicio;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblProveedor;

    @FXML
    private Label lblContrato;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblMonto;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblFecha;

    private final SearchService searchService =
            new SearchService();

    @FXML
    public void initialize() {

        cargarServicios();

        cmbServicios.setOnAction(
                event -> mostrarServicio()
        );

        txtContrato.setOnAction(
                event -> buscarDetallePago()
        );
    }

    /**
     * Carga todos los servicios disponibles.
     */
    private void cargarServicios() {

        List<ServiceCatalog> servicios =
                searchService.buscarServicios("", "Todos");

        cmbServicios.setItems(
                FXCollections.observableArrayList(servicios)
        );
    }

    /**
     * Muestra información básica del servicio seleccionado.
     */
    private void mostrarServicio() {

        ServiceCatalog service =
                cmbServicios.getSelectionModel().getSelectedItem();

        if (service == null) {
            return;
        }

        lblServicio.setText(service.getNombre());
        lblTipo.setText(service.getTipo());
        lblProveedor.setText(service.getProveedor());


        limpiarResultadoPago();
    }

    /**
     * Busca el detalle de pago usando:
     * service_id + account_number.
     */
    @FXML
    private void buscarDetallePago() {

        ServiceCatalog service =
                cmbServicios
                        .getSelectionModel()
                        .getSelectedItem();

        if (service == null) {

            limpiarResultadoPago();

            mostrarAlerta(
                    "Debe seleccionar un servicio"
            );

            return;
        }

        String contrato = txtContrato.getText();

        if (contrato == null || contrato.isBlank()) {

            limpiarResultadoPago();

            mostrarAlerta(
                    "Debe ingresar número de contrato"
            );

            return;
        }

        ServicePaymentDetail detail =
                searchService.obtenerDetallePago(
                        Long.parseLong(service.getId()),
                        contrato.trim()
                );

        if (detail == null) {

            limpiarResultadoPago();

            mostrarAlerta(
                    "No existe contrato relacionado "
                            + "con el servicio"
            );

            return;
        }

        mostrarResultado(detail);
    }

    /**
     * Muestra el detalle de la deuda encontrada.
     *
     * @param detail contiene la información de la deuda
     */
    private void mostrarResultado(
            ServicePaymentDetail detail
    ) {

        lblContrato.setText(
                detail.getAccountNumber()
        );

        NumberFormat format =
                NumberFormat.getCurrencyInstance(
                        new Locale("es", "CO")
                );

        lblUsuario.setText(
                detail.getUsuario()
        );

        lblMonto.setText(
                format.format(detail.getMonto())
        );

        lblEstado.setText(
                detail.getEstado()
        );

        lblFecha.setText(
                detail.getFechaVencimiento()
        );
    }

    /**
     * Limpia todo el formulario.
     */
    @FXML
    private void limpiarFormulario() {

        cmbServicios.getSelectionModel().clearSelection();

        txtContrato.clear();
        lblUsuario.setText("");
        lblServicio.setText("");
        lblTipo.setText("");
        lblProveedor.setText("");

        limpiarResultadoPago();
    }

    /**
     * Limpia solamente el resultado del pago.
     */
    private void limpiarResultadoPago() {

        lblContrato.setText("");
        lblUsuario.setText("");
        lblMonto.setText("");
        lblEstado.setText("");
        lblFecha.setText("");
    }

    /**
     * Muestra alerta simple.
     *
     * @param mensaje texto a mostrar en la alerta
     */
    private void mostrarAlerta(String mensaje) {

        Alert alert =
                new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    @FXML
    private void onVolverClick() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.MAIN.getView()
        );
    }
}

