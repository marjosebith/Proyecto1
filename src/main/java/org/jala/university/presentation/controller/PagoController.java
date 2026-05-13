package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.jala.university.application.service.SearchService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.domain.entity.UserService;
import org.jala.university.domain.repository.ServiceRepository;
import org.jala.university.presentation.ExternalPaymentView;

import java.util.List;

public final class PagoController extends BaseController {

    @FXML
    private ComboBox<UserService> comboServicios;
    private final ServiceRepository service = new SearchService();

    @FXML
    public void initialize() {
        cargarServicios();

        comboServicios.setOnAction(e -> {
            UserService seleccionado = comboServicios.getValue();
            if (seleccionado != null) {
                System.out.println("Seleccionado: " + seleccionado.getServiceName());
            }
        });
    }

    private void cargarServicios() {
        List<UserService> lista = service.listarServiciosPorUsuario(1);
        comboServicios.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void onVolverClick() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.MAIN.getView()
        );
    }
}
