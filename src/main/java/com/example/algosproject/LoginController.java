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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginController implements Initializable {
    @FXML Text empty_login;
    @FXML Text empty_pass;
    @FXML Button sign;
    @FXML Button login_button;
    @FXML TextField login;
    @FXML TextField password;
    @FXML
    CheckBox remember_me;
    @FXML Text forgot;
    @FXML
    ProgressBar progressBar;
    String data = "";
    boolean can_login = false;
    boolean remember = false;
    File database;
    File without_login = new File("src/main/rememberme.txt");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login_button.setOnMouseClicked(event -> {
            database = new File("src/main/Database/file.txt");
            if (database.exists()) {
                if (remember_me.isSelected()) {
                    remember = true;
                }
                logic();
                if (remember && can_login) {
                    try {
                        File file = new File("src/main/UserLoggedIn.txt");
                        file.createNewFile();
                        RandomAccessFile randomAccessFile = new RandomAccessFile(without_login, "rw");
                        randomAccessFile.writeBytes("username: " + login.getText() + "\n" +
                                "password: " + password.getText());
                        randomAccessFile.close();
                        Duration duration = Duration.seconds(1);
                        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 1.0);
                        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
                        Timeline timeline = new Timeline(keyFrame);
                        timeline.setOnFinished(event1 -> {
                            try {
                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("userpage.fxml")));
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        });
                        timeline.play();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if(!remember && can_login){
                    Duration duration = Duration.seconds(1);
                    KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 1.0);
                    KeyFrame keyFrame = new KeyFrame(duration, keyValue);
                    Timeline timeline = new Timeline(keyFrame);
                    timeline.setOnFinished(event1 -> {
                        try {
                            File file = new File("src/main/UserLoggedIn.txt");
                            file.createNewFile();
                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("userpage.fxml")));
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
                    timeline.play();
                }
            } else {
                System.out.println("File doesn't exist!");
            }
        });
        sign.setOnMouseClicked(event -> {
            Duration duration = Duration.seconds(1);
            KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 1.0);
            KeyFrame keyFrame = new KeyFrame(duration, keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.setOnFinished(event1 -> {
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signup.fxml")));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            timeline.play();
        });
        forgot.setOnMouseClicked(event -> {
            Duration duration = Duration.seconds(1);
            KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 1.0);
            KeyFrame keyFrame = new KeyFrame(duration, keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.setOnFinished(event1 -> {
                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("password-reset.fxml")));
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

    public void logic(){
        empty_login.setVisible(login.getText().isEmpty());
        empty_pass.setVisible(password.getText().isEmpty());
        try {
            Scanner in = new Scanner(database);
            while (in.hasNextLine()) {
                data = data + in.nextLine() + "\n";
            }
            if (data.contains("username: " + login.getText() + "\n" +
                    "password: " + password.getText())) {
                can_login = true;
            } else {
                if(!login.getText().isEmpty() && !password.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong username/password", ButtonType.CLOSE);
                    alert.setHeaderText("Error Occurred!");
                    alert.showAndWait();
                }
            }
        } catch (FileNotFoundException e) {
            try {
                throw new IOException(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

