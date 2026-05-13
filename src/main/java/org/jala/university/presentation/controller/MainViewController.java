package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.presentation.ExternalPaymentView;

public class MainViewController extends BaseController {

    @FXML
    private void goToEdit() {
        ViewSwitcher.switchTo(ExternalPaymentView.EDIT.getView());
    }

    @FXML
    private void irABuscarServicio() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.BUSCAR_SERVICIO.getView()
        );
    }

    @FXML
    private void goToRegister() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.REGISTRAR_SERVICIO.getView()
        );
    }

    @FXML
    private void goToVerService() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.VER_SERVICIO.getView()
        );
    }

    @FXML
    private void goToDelete() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.ELIMINAR_SERVICIO.getView()
        );
    }

    /**
     * Navigates to the payment view.
     */
    @FXML
    public void irAPago() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.PAGO.getView()
        );
    }
}
