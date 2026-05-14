package org.jala.university.presentation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import org.jala.university.application.service.CreateService;
import org.jala.university.commons.presentation.BaseController;

import java.util.function.Consumer;

public class FormularioServicioController extends BaseController {

    @FXML private TextField fieldNombre;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField fieldProveedor;
    @FXML private TextField fieldCategoria;

    @FXML private Label errorNombre;
    @FXML private Label errorTipo;
    @FXML private Label errorProveedor;
    @FXML private Label errorCategoria;

    private final CreateService service = new CreateService();

    @Setter
    private Consumer<String> onRegistroExitoso;

    @FXML
    public final void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList(
                "utilities", "telecom", "entertainment"
        ));
    }

    @FXML
    private void handleConfirmar() {
        limpiarErrores();

        if (!validarCampos()) {
            return;
        }

        try {
            service.registrarServicio(
                    fieldNombre.getText().trim(),
                    comboTipo.getValue(),
                    fieldProveedor.getText().trim(),
                    fieldCategoria.getText().trim()
            );

            if (onRegistroExitoso != null) {
                onRegistroExitoso.accept(fieldNombre.getText().trim());
            }
            cerrarVentana();

        } catch (IllegalArgumentException e) {
            errorNombre.setText(e.getMessage());
        } catch (Exception e) {
            errorNombre.setText("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    private boolean validarCampos() {
        boolean valido = true;

        if (fieldNombre.getText().isBlank()) {
            errorNombre.setText("Requerido");
            valido = false;
        }
        if (comboTipo.getValue() == null) {
            errorTipo.setText("Requerido");
            valido = false;
        }
        if (fieldProveedor.getText().isBlank()) {
            errorProveedor.setText("Requerido");
            valido = false;
        }
        if (fieldCategoria.getText().isBlank()) {
            errorCategoria.setText("Requerido");
            valido = false;
        }

        return valido;
    }

    private void limpiarErrores() {
        errorNombre.setText("");
        errorTipo.setText("");
        errorProveedor.setText("");
        errorCategoria.setText("");
    }

    private void cerrarVentana() {
        ((Stage) fieldNombre.getScene().getWindow()).close();
    }
}
