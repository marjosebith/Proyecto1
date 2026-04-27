package org.jala.university.presentation.controller;

import org.jala.university.application.dto.UserServiceDto;
import org.jala.university.application.service.UserServiceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller para UserServices.fxml.
 * Muestra los servicios del usuario en sesión y permite abrir el editor.
 */
public class UserServicesController implements Initializable {

    // ── FXML ──────────────────────────────────────────────────────────────────
    @FXML private Label                       lblUserName;
    @FXML private TableView<UserServiceDto>   tblServices;
    @FXML private TableColumn<UserServiceDto, String>  colAlias;
    @FXML private TableColumn<UserServiceDto, String>  colServiceName;
    @FXML private TableColumn<UserServiceDto, String>  colProvider;
    @FXML private TableColumn<UserServiceDto, String>  colCategory;
    @FXML private TableColumn<UserServiceDto, String>  colAccount;
    @FXML private TableColumn<UserServiceDto, Integer> colStatus;
    @FXML private TableColumn<UserServiceDto, Void>    colActions;
    @FXML private Label                       lblCount;

    // ── Dependencias ──────────────────────────────────────────────────────────
    private final UserServiceService service = new UserServiceService();

    /** Usuario en sesión — asignado desde la pantalla de login */
    private int currentUserId = 1; // reemplaza con tu session manager

    // ──────────────────────────────────────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupColumns();
        loadData();
    }

    /** Permite inyectar el usuario desde fuera (p.ej. al navegar desde login) */
    public void setCurrentUser(int userId, String userName) {
        this.currentUserId = userId;
        lblUserName.setText(userName);
        loadData();
    }

    // ── Setup columnas ────────────────────────────────────────────────────────

    private void setupColumns() {
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colAlias.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String alias, boolean empty) {
                super.updateItem(alias, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null); return;
                }
                UserServiceDto dto = getTableRow().getItem();
                setText(alias != null && !alias.isBlank() ? alias : dto.getServiceName());
            }
        });

        colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        colProvider.setCellValueFactory(new PropertyValueFactory<>("providerName"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAccount.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        // Columna estado — badge de color
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        colStatus.setCellFactory(col -> new TableCell<>() {
            private final Label badge = new Label();
            @Override protected void updateItem(Integer val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }
                boolean active = val == 1;
                badge.setText(active ? "Activo" : "Inactivo");
                badge.getStyleClass().setAll(active ? "badge-active" : "badge-inactive");
                setGraphic(badge);
            }
        });

        // Columna acciones — botón Editar
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Editar");
            {
                btnEdit.getStyleClass().add("btn-edit-row");
                btnEdit.setOnAction(e -> {
                    UserServiceDto dto = getTableView().getItems().get(getIndex());
                    openEditDialog(dto);
                });
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btnEdit);
            }
        });
    }

    // ── Carga de datos ────────────────────────────────────────────────────────

    private void loadData() {
        try {
            List<UserServiceDto> items = service.getUserServices(currentUserId);
            tblServices.setItems(FXCollections.observableArrayList(items));
            lblCount.setText(items.size() + " servicio(s) registrado(s)");
        } catch (Exception ex) {
            showAlert("Error", "No se pudieron cargar los servicios:\n" + ex.getMessage());
        }
    }

    // ── Abrir diálogo de edición ──────────────────────────────────────────────

    private void openEditDialog(UserServiceDto dto) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/jala/university/view/EditUserService.fxml"));
            Parent root = loader.load();

            EditUserServiceController editCtrl = loader.getController();
            editCtrl.setUserService(dto);
            editCtrl.setOnSaved(this::loadData);

            Stage dialog = new Stage();
            dialog.setTitle("Editar Servicio");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tblServices.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/org/jala/university/view/services.css").toExternalForm());
            dialog.setScene(scene);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException ex) {
            showAlert("Error", "No se pudo abrir el editor:\n" + ex.getMessage());
        }
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}