package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum ExternalPaymentView {

    MAIN("fxml/main-view.fxml"),
    EDIT("fxml/services/editar-servicio.fxml"),
    BUSCAR_SERVICIO("fxml/services/buscar-servicio.fxml"),
    REGISTRAR_SERVICIO("fxml/services/registrar-servicio.fxml"),
    VER_SERVICIO("fxml/services/ver-servicio.fxml");

    private final View view;

    ExternalPaymentView(String fileName) {
        this.view = new View(fileName);
    }
}
