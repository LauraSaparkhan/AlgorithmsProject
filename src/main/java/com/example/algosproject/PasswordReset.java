package com.example.algosproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PasswordReset implements Initializable {
    @FXML
    ProgressBar progressBar;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    Button confirm;
    @FXML
    Button back;
    boolean ok = false;

    ArrayList<String> users = new ArrayList<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirm.setOnMouseClicked(event -> {
            String data = "";
            File file = new File("src/main/Database/file.txt");
            if(!username.getText().isEmpty() && !password.getText().isEmpty()) {
                try {
                    Scanner in = new Scanner(file);
                    while (in.hasNextLine()) {
                        data = in.nextLine();
                        users.add(data);
                    }
                    in.close();
                    if (users.contains("username: " + username.getText())) {
                        int index = users.indexOf("username: " + username.getText());
                        users.add(index + 1, "password: " + password.getText());
                        users.remove(index + 2);
                        if (file.delete()) {
                            PrintWriter printWriter = new PrintWriter("src/main/Database/file.txt");
                            for (int i = 0; i < users.size(); i++) {
                                printWriter.println(users.get(i));
                            }
                            printWriter.close();
                            ok = true;
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please try again!", ButtonType.CLOSE);
                        alert.setHeaderText("You have entered wrong username!");
                        alert.show();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if (ok) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your password has been changed! :)", ButtonType.CLOSE);
                    alert.setHeaderText("The operation has been successfully managed!");
                    alert.show();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please try again!", ButtonType.CLOSE);
                alert.setHeaderText("Please fill out all fields!");
                alert.show();
            }
        });

        back.setOnMouseClicked(event -> {
            Duration duration = Duration.seconds(1);
            KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 1.0);
            KeyFrame keyFrame = new KeyFrame(duration, keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.setOnFinished(event1 -> {
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            timeline.play();
        });
    }
}
