package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.jala.university.application.service.ExternalPaymentService;
import org.jala.university.application.service.ExternalPaymentServiceImpl;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.entity.Servicio;
import org.jala.university.presentation.ExternalPaymentView;

import java.util.List;

public final class PagoController extends BaseController {

    @FXML
    private ComboBox<Servicio> comboServicios;

    private final ExternalPaymentService service = new ExternalPaymentServiceImpl();

    @FXML
    public void initialize() {
        cargarServicios();

        comboServicios.setOnAction(e -> {
            Servicio seleccionado = comboServicios.getValue();
            if (seleccionado != null) {
                System.out.println("Seleccionado: " + seleccionado.getNombre());
            }
        });
    }

    private void cargarServicios() {
        List<Servicio> lista = service.listarServicios();
        comboServicios.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void onVolverClick() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.MAIN.getView()
        );
    }
}
