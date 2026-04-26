package org.jala.university.presentation;

import org.jala.university.commons.presentation.View;

public enum ExternalPaymentView implements View {

    MAIN("main-view.fxml"),
    BUSCAR_SERVICIO("buscar-servicio.fxml");

    private final String fileName;

    ExternalPaymentView(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
