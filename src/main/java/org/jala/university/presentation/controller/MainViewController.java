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
}
