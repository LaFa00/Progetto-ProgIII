package com.example.mailclient.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {

    public void start(Stage stage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/example/mailclient/login.fxml"));
        Scene scene = new Scene(loginLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
