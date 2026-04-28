package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

import javax.swing.*;
import java.awt.*;

@Getter
public enum ExternalPaymentView {

    MAIN("fxml/main-view.fxml"),
    EDIT("fxml/services/editar-servicio.fxml");

    private final View view;

    ExternalPaymentView(String fileName) {
        this.view = new View(fileName);
    }
}
