package org.jala.university.presentation;

import org.jala.university.commons.presentation.View;

public enum ExternalPaymentView {

    MAIN("main-view.fxml"),
    BUSCAR_SERVICIO("buscar-servicio.fxml");

    private final View view;

    ExternalPaymentView(String fileName) {
        this.view = new View(fileName);
    }

    public View getView() {
        return view;
    }
}
