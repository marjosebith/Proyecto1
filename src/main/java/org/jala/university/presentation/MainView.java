package org.jala.university.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public final class MainView extends Application {
        private static final int WIDTH = 600;
        private static final int HEIGHT = 400;

        @Override
        public void start(Stage stage) throws Exception {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/forms/ServicesView.fxml")
            );

            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);

            stage.setTitle("HU28 Test");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
