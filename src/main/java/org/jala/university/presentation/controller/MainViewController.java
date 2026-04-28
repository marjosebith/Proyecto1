package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.presentation.ExternalPaymentView;

public final class MainViewController extends BaseController {

    @FXML
    public void irABuscarServicio() {
        ViewSwitcher.switchTo(
                ExternalPaymentView.BUSCAR_SERVICIO.getView()
        );
    }
}
