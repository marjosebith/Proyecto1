package org.jala.university.presentation.controller;

import lombok.Setter;
import org.jala.university.application.dto.UserServiceDto;
import org.jala.university.application.service.UserServiceService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller para EditUserService.fxml.
 * Prelllena el formulario con los datos del user_service y persiste los cambios.
 */
public class EditUserServiceController implements Initializable {

    // ── FXML ──────────────────────────────────────────────────────────────────
    @FXML private Label    lblServiceInfo;
    @FXML private Label    lblBadge;
    @FXML private Label    infoServiceName;
    @FXML private Label    infoType;
    @FXML private Label    infoProvider;
    @FXML private Label    infoCategory;

    @FXML private TextField txtAlias;
    @FXML private TextField txtAccountNumber;
    @FXML private TextArea  txtNotes;
    @FXML private CheckBox  chkActive;

    @FXML private Label     lblEstadoText;
    @FXML private Label     lblFeedback;
    @FXML private Label     errAlias;
    @FXML private Label     errAccount;
    @FXML private Label     errNotes;
    @FXML private Button    btnSave;

    // ── Dependencias ──────────────────────────────────────────────────────────
    private final UserServiceService service = new UserServiceService();
    private UserServiceDto currentDto;
    /**
     * -- SETTER --
     * Callback que se invoca tras guardar con éxito
     */
    @Setter
    private Runnable onSaved; // callback para refrescar la tabla

    // ──────────────────────────────────────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Limpiar errores al escribir
        txtAlias.textProperty().addListener((o, ov, nv) -> hideError(errAlias));
        txtAccountNumber.textProperty().addListener((o, ov, nv) -> hideError(errAccount));
        txtNotes.textProperty().addListener((o, ov, nv) -> hideError(errNotes));
    }

    /** Inyecta el DTO y precarga el formulario */
    public void setUserService(UserServiceDto dto) {
        this.currentDto = dto;
        populateForm(dto);
    }

    // ── Poblar formulario ─────────────────────────────────────────────────────

    private void populateForm(UserServiceDto dto) {
        // Header
        lblServiceInfo.setText(dto.getServiceName() + " · " + dto.getProviderName());
        updateBadge(dto.getIsActive() == 1);

        // Info de solo lectura
        infoServiceName.setText(dto.getServiceName());
        infoType.setText(dto.getServiceType());
        infoProvider.setText(dto.getProviderName());
        infoCategory.setText(dto.getCategory() != null ? dto.getCategory() : "—");

        // Campos editables
        txtAlias.setText(dto.getAlias() != null ? dto.getAlias() : "");
        txtAccountNumber.setText(dto.getAccountNumber() != null ? dto.getAccountNumber() : "");
        txtNotes.setText(dto.getNotes() != null ? dto.getNotes() : "");

        boolean active = dto.getIsActive() == 1;
        chkActive.setSelected(active);
        lblEstadoText.setText(active ? "Activo" : "Inactivo");
    }

    // ── Toggle estado ─────────────────────────────────────────────────────────

    @FXML
    private void handleToggleEstado() {
        boolean active = chkActive.isSelected();
        lblEstadoText.setText(active ? "Activo" : "Inactivo");
        updateBadge(active);
    }

    private void updateBadge(boolean active) {
        lblBadge.setText(active ? "Activo" : "Inactivo");
        lblBadge.getStyleClass().setAll(active ? "badge-active" : "badge-inactive");
    }

    // ── Guardar ───────────────────────────────────────────────────────────────

    @FXML
    private void handleSave() {
        if (!validate()) return;

        // Mapear campos al DTO
        currentDto.setAlias(txtAlias.getText().trim());
        currentDto.setAccountNumber(
                txtAccountNumber.getText().trim().isBlank() ? null
                        : txtAccountNumber.getText().trim());
        currentDto.setNotes(txtNotes.getText().trim().isBlank() ? null
                : txtNotes.getText().trim());
        currentDto.setIsActive(chkActive.isSelected() ? 1 : 0);

        btnSave.setDisable(true);
        btnSave.setText("Guardando…");

        // Ejecutar en hilo de fondo para no bloquear la UI
        new Thread(() -> {
            try {
                service.update(currentDto);

                Platform.runLater(() -> {
                    showFeedback("✓ Cambios guardados correctamente", true);
                    if (onSaved != null) onSaved.run();
                    // Cerrar el dialog después de un momento
                    new Thread(() -> {
                        try { Thread.sleep(900); } catch (InterruptedException ignored) {}
                        Platform.runLater(this::closeWindow);
                    }).start();
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showFeedback("✗ " + ex.getMessage(), false);
                    btnSave.setDisable(false);
                    btnSave.setText("Guardar cambios");
                });
            }
        }).start();
    }

    // ── Cancelar ──────────────────────────────────────────────────────────────

    @FXML
    private void handleBack() {
        closeWindow();
    }

    // ── Validación ────────────────────────────────────────────────────────────

    private boolean validate() {
        boolean ok = true;
        clearFeedback();

        String alias = txtAlias.getText().trim();
        if (alias.length() > 100) {
            showError(errAlias, "Máximo 100 caracteres");
            ok = false;
        }

        String notes = txtNotes.getText().trim();
        if (notes.length() > 500) {
            showError(errNotes, "Máximo 500 caracteres");
            ok = false;
        }

        return ok;
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    private void showError(Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

    private void hideError(Label lbl) {
        lbl.setVisible(false);
        lbl.setManaged(false);
    }

    private void showFeedback(String msg, boolean ok) {
        lblFeedback.setText(msg);
        lblFeedback.getStyleClass().setAll("feedback-label", ok ? "feedback-ok" : "feedback-err");
    }

    private void clearFeedback() {
        lblFeedback.setText("");
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}