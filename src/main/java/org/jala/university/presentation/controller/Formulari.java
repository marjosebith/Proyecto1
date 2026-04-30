package org.jala.university.presentation.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.infrastructure.config.ConnectionManager;

public class Formulari extends BaseController {

    @FXML
    private TextField fieldNombre;

    @FXML
    private ComboBox<String> comboTipo;

    @FXML
    private TextField fieldProveedor;

    @FXML
    private TextField fieldCategoria;

    @FXML
    private Label errorNombre;

    @FXML
    private Label errorTipo;

    @FXML
    private Label errorProveedor;

    @FXML
    private Label errorCategoria;

    private Consumer<String> onRegistroExitoso;

    private static final String INSERT_SQL =

            "INSERT INTO services (service_name, service_type, provider_name, category)"
                    + " VALUES (?, ?, ?, ?)";

    private static final int COL_NOMBRE = 1;
    private static final int COL_TIPO = 2;
    private static final int COL_PROVEEDOR = 3;
    private static final int COL_CATEGORIA = 4;

    @FXML
    public final void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList(
                "utilities",
                "telecom",
                "entertainment"
        ));
    }

    public final void setOnRegistroExitoso(final Consumer<String> callback) {
        this.onRegistroExitoso = callback;
    }

    @FXML
    public final void handleConfirmar() {
        limpiarErrores();
        if (!validarCampos()) {
            return;
        }
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setString(COL_NOMBRE, fieldNombre.getText().trim());
            stmt.setString(COL_TIPO, comboTipo.getValue());
            stmt.setString(COL_PROVEEDOR, fieldProveedor.getText().trim());
            stmt.setString(COL_CATEGORIA, fieldCategoria.getText().trim());
            stmt.executeUpdate();
            if (onRegistroExitoso != null) {
                onRegistroExitoso.accept(fieldNombre.getText().trim());
            }
            cerrarVentana();
        } catch (SQLException e) {
            errorNombre.setText("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    public final void handleCancelar() {
        cerrarVentana();
    }

    private boolean validarCampos() {
        boolean valido = true;
        if (fieldNombre.getText() == null || fieldNombre.getText().isBlank()) {
            errorNombre.setText("El nombre no puede estar vacio.");
            valido = false;
        }
        if (comboTipo.getValue() == null) {
            errorTipo.setText("Selecciona un tipo de servicio.");
            valido = false;
        }
        if (fieldProveedor.getText() == null || fieldProveedor.getText().isBlank()) {
            errorProveedor.setText("El proveedor no puede estar vacio.");
            valido = false;
        }
        if (fieldCategoria.getText() == null || fieldCategoria.getText().isBlank()) {
            errorCategoria.setText("La categoria no puede estar vacia.");
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
        Stage stage = (Stage) fieldNombre.getScene().getWindow();
        stage.close();
    }
}

