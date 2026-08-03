package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ui.MainLayout;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        MainLayout mainLayout = new MainLayout();

        Scene scene = new Scene(
                mainLayout.create(),
                1200,
                750
        );

        stage.setTitle("Sparepart Finance");

        stage.setScene(scene);

        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}