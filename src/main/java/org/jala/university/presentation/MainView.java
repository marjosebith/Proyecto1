package org.jala.university.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public final class MainView extends Application {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainView.class.getResource("/forms/ServicesView.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle(" Ver Servicios");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
