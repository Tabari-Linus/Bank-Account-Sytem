package com.example.bankaccountmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Set window size after the scene is loaded
        stage.setTitle("Bank Account Management");
        stage.setScene(scene);
        stage.setWidth(800); // Set the width
        stage.setHeight(600); // Set the height
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
