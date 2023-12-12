package com.example.algosproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage)  {
        LoginController loginController = new LoginController();
        try {
            if(loginController.without_login.length() > 0){
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("userpage.fxml")));
                primaryStage.setTitle("Medication Reminder");
                primaryStage.setScene(new Scene(root, 700, 500));
            }
            else {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
                primaryStage.setTitle("Medication Reminder");
                primaryStage.setScene(new Scene(root, 700, 500));
            }
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}