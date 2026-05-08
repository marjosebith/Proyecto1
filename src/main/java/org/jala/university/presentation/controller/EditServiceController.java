package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.jala.university.application.service.UpdateService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewContext;
import org.jala.university.domain.entity.UserService;
import org.jala.university.infrastructure.persistence.service.ServiceRepositoryImpl;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.presentation.ExternalPaymentView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class EditServiceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditServiceController.class);

    @FXML private VBox servicesContainer;

    @FXML private TextField aliasField;
    @FXML private TextField accountField;
    @FXML private TextField notesField;
    @FXML private CheckBox activeCheck;

    private final UpdateService serviceService =
            new UpdateService(new ServiceRepositoryImpl());

    private UserService currentService;

    @FXML
    private void initialize() {
        loadServices();
    }

    private void loadServices() {
        servicesContainer.getChildren().clear();

        List<UserService> services = serviceService.getServicesByUser(1);

        if (services.isEmpty()) {
            servicesContainer.getChildren().add(
                    new Label("No tienes servicios registrados.")
            );
            return;
        }

        ToggleGroup group = new ToggleGroup();

        for (UserService s : services) {
            String label = s.getServiceName()
                    + (s.getAlias() != null ? " — " + s.getAlias() : "")
                    + (s.getAccountNumber() != null ? " [" + s.getAccountNumber() + "]" : "");

            ToggleButton btn = new ToggleButton(label);
            btn.setToggleGroup(group);
            btn.setMaxWidth(Double.MAX_VALUE);

            btn.setOnAction(e -> {
                if (btn.isSelected()) {
                    currentService = s;
                    aliasField.setText(s.getAlias() != null ? s.getAlias() : "");
                    accountField.setText(s.getAccountNumber() != null ? s.getAccountNumber() : "");
                    notesField.setText(s.getNotes() != null ? s.getNotes() : "");
                    activeCheck.setSelected(s.isActive());
                } else {
                    currentService = null;
                    clearForm();
                }
            });

            servicesContainer.getChildren().add(btn);
        }
    }

    private void clearForm() {
        aliasField.clear();
        accountField.clear();
        notesField.clear();
        activeCheck.setSelected(false);
    }

    @FXML
    private void handleUpdate() {
        if (currentService == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selecciona un servicio");
            alert.setHeaderText("Selecciona un servicio primero");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cambios");
        alert.setHeaderText("¿Estás seguro de guardar los cambios?");
        alert.setContentText(String.format(
                "Servicio: %s%nAlias: %s%nCuenta: %s",
                currentService.getServiceName(),
                aliasField.getText(),
                accountField.getText()
        ));

        ButtonType btnGuardar  = new ButtonType("Guardar",   ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar",  ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnGuardar, btnCancelar);

        alert.initOwner(servicesContainer.getScene().getWindow());

        alert.showAndWait().ifPresent(response -> {
            if (response == btnGuardar) {
                currentService.setAlias(aliasField.getText());
                currentService.setAccountNumber(accountField.getText());
                currentService.setNotes(notesField.getText());
                currentService.setIsActive(activeCheck.isSelected() ? 1 : 0);

                try {
                    serviceService.update(currentService);
                    LOGGER.info("Actualizado correctamente");
                    ViewSwitcher.switchTo(ExternalPaymentView.MAIN.getView());
                } catch (Exception e) {
                    mostrarError(e.getMessage());
                }
            }
            // Si cancela, simplemente no hace nada
        });
    }

    private void mostrarError(String mensaje) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error al guardar");
        error.setHeaderText("No se pudieron guardar los cambios");
        error.setContentText(mensaje);
        error.initOwner(servicesContainer.getScene().getWindow());
        error.showAndWait();
    }

    @FXML private void goBack() {
        ViewSwitcher.switchTo(ExternalPaymentView.MAIN.getView());
    }

    @Override
    public void setContext(ViewContext context) {
        // si no necesitas contexto puedes dejarlo vacío
    }
}
