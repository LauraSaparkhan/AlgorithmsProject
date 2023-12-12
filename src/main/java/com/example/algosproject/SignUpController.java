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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SignUpController implements Initializable {
    boolean user = false, mailbox = false;
    @FXML
    Text limit_user;
    @FXML
    Text limit_password;
    @FXML
    Text limit_email;
    @FXML
    Text empty_username;
    @FXML
    Text empty_password;
    @FXML
    Text empty_email;
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    PasswordField email;
    @FXML
    Button sign_up;
    @FXML
    Button login;
    @FXML
    ProgressBar progressBar;
    File database;
    String temp;
    String temp2 = "";
    boolean ok = false;
    boolean ok1 = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sign_up.setOnMouseClicked(event -> {
            boolean log = !username.getText().isEmpty();
            boolean mail = !email.getText().isEmpty();
            boolean pass = !password.getText().isEmpty();
            empty_username.setVisible(username.getText().isEmpty());
            empty_password.setVisible(password.getText().isEmpty());
            empty_email.setVisible(email.getText().isEmpty());
            if(log && pass && mail) {
                createFolder();
                database = new File("src/main/Database/file.txt");
                readFile();
                temp = "username: " + username.getText() + "\n" + "password: " + password.getText() + "\n" + "email: " + email.getText();
                try {
                    checkData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    database.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                countLines();
                addData();
                if(ok && ok1) {
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
                }
            }
        });
        login.setOnAction(event -> {
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
    public void createFolder(){
        String directory = "src/main/Database";
        File file = new File(directory);
        if(!file.exists()){
            if(file.mkdir()){
                System.out.println("Folder has been created successfully!");
            }
            else System.out.println("Folder failed to be created :(");
        } else System.out.println("Folder already exists!");

    }
    public void readFile(){
        File file = database;
        if(file.exists()){
            try {
                throw new FileNotFoundException(null);
            } catch (FileNotFoundException e) {
                System.out.println("File exists!");
            }
        }else{
            try{
                throw new IOException((String) null);
            } catch (IOException e) {
                System.out.println("File created!");
            }
        }
    }
    public void addData() {
        try {
            String add = "";
            Scanner in = new Scanner(database);
            while(in.hasNextLine()){
                add = add + in.nextLine() + "\n";
            }
            in.close();
            RandomAccessFile randomAccessFile = new RandomAccessFile(database, "rw");
            randomAccessFile.writeBytes(add + temp2);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkData() throws IOException {
        if(!username.getText().isEmpty()) {
            if (username.getText().length() < 5 || username.getText().length() > 9) {
                limit_user.setVisible(true);
            }
            else limit_user.setVisible(false);
        }
        if(!password.getText().isEmpty()) {
            if (password.getText().length() < 5 || password.getText().length() > 9) {
                limit_password.setVisible(true);
            } else limit_password.setVisible(false);
        }
        if(!email.getText().isEmpty()) {
            if (email.getText().length() < 5 || email.getText().length() > 9) {
                limit_email.setVisible(true);
            } else limit_email.setVisible(false);
        }
        if(!limit_email.isVisible() && !limit_password.isVisible() && !limit_user.isVisible()){
            temp2 = temp;
            ok1 =true;
        }
        try {
            throw new FileNotFoundException(null);
        } catch (FileNotFoundException e) {
            System.out.println();
        }
        try {
            throw new IOException((String) null);
        } catch (IOException e) {
            System.out.println();
        }
    }
    public void countLines() {
        Scanner in;
        int num = 0;
        try {
            in = new Scanner(database);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String data = "";
        while(in.hasNextLine()){
            num++;
            data = data + in.nextLine() + "\n";
        }
        System.out.println("Number of lines: " + num);
        in.close();
        if(data.contains("username: " + username.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR, "This username is already signed up in our database! Please try another one!", ButtonType.CLOSE);
            alert.setHeaderText("Error Occurred!");
            alert.showAndWait();
            temp2 = "";
        } else user = true;
        if(user && data.contains("email: " + email.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR, "This email is already signed up in our database! Please try another one!", ButtonType.CLOSE);
            alert.setHeaderText("Error Occurred!");
            alert.showAndWait();
            temp2 = "";
        } else mailbox = true;
        if(mailbox && user){
            ok = true;
        }
        if(!database.exists()){
            try {
                throw new FileNotFoundException(null);
            } catch (FileNotFoundException e) {
                System.out.println("File doesn't exist!");
            }
        }
        if(!database.isFile()){
            try {
                throw new IOException((String) null);
            } catch (IOException e) {
                System.out.println("File must have been removed or lost.");
            }
        }
    }
}
