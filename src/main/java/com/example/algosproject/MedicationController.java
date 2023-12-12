package com.example.algosproject;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.*;

public class MedicationController implements Initializable {
    TableView<Medication> tableView = new TableView<>();
    TableColumn<Medication, String> nameColumn = new TableColumn<>("The Name of the medication");
    TableColumn<Medication, String> doseColumn = new TableColumn<>("The Dose of the medication");
    TableColumn<Medication, String> timeColumn = new TableColumn<>("Time you need to take it");
    @FXML
    private TextField medNameField;
    @FXML
    private TextField dose;
    @FXML
    private TextField time;
    @FXML
    private Button create;
    @FXML
    private Button view;
    @FXML
    private Button leaveButton;
    @FXML
    private ProgressBar progressBar;
    private Map<String, Timer> medicationTimers;

    public MedicationController() {
        medicationTimers = new HashMap<>();
    }
    @FXML
    public void addMedication() {
        String name = medNameField.getText();
        String doseText = dose.getText();
        String reminderTime = time.getText();
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        doseColumn.setCellValueFactory(cellData -> cellData.getValue().doseProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(doseColumn);
        tableView.getColumns().add(timeColumn);

        if (!name.isEmpty() && !doseText.isEmpty() && !reminderTime.isEmpty()) {
            try {
                LocalTime.parse(reminderTime);
                if (!medicationTimers.containsKey(name)) {
                    scheduleReminder(name, doseText, reminderTime);
                    tableView.getItems().add(new Medication(name, doseText, reminderTime));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Medication '" + name + "' has been successfully added!",
                            ButtonType.OK);
                    alert.showAndWait();
                    medNameField.clear();
                    dose.clear();
                    time.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "Medication '" + name + "' already has a reminder scheduled.",
                            ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Invalid input format. Please check dose and time format.",
                        ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Please fill in all fields (Name, Dose, Time).",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void scheduleReminder(String medicationName, String doseValue, String reminderTime) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Medication Reminder");
                    alert.setHeaderText(null);
                    alert.setContentText("Time to take " + medicationName + " (" + doseValue + "mg)");
                    alert.showAndWait();
                });
            }
        }, calculateDelay(reminderTime));
        medicationTimers.put(medicationName, timer);
    }

    private long calculateDelay(String time) {
        LocalTime now = LocalTime.now();
        LocalTime reminderTime = LocalTime.parse(time);
        long delay = reminderTime.toSecondOfDay() - now.toSecondOfDay();

        if (delay < 0) {
            // If the time has already passed today, schedule it for tomorrow
            delay += 24 * 60 * 60; // 24 hours in seconds
        }
        return delay * 1000; // Convert delay to milliseconds
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leaveButton.setOnAction(event -> {
            File file = new File("src/main/rememberme.txt");
            file.delete();
            Duration duration = Duration.seconds(1);
            KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 1.0);
            KeyFrame keyFrame = new KeyFrame(duration, keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.play();
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
        });
        view.setOnAction(event -> {
            nameColumn.setSortable(true);
            tableView.setOnSort(event1 -> {
                tableView.setItems(bubbleSort(tableView.getItems()));
            });
            TextField searchField = new TextField();
            searchField.setPromptText("Search by name...");
            Button searchButton = new Button("Search");
            searchButton.setOnAction(event1 -> {
                String searchName = searchField.getText().trim();
                Medication foundMedication = linearSearch(tableView.getItems(), searchName);
                if (foundMedication != null) {
                    tableView.getSelectionModel().select(foundMedication);
                    tableView.scrollTo(foundMedication);
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "No medication with the name " + searchName + " found!",
                            ButtonType.OK);
                    alert.showAndWait();
                }
            });
            HBox searchLayout = new HBox(searchField, searchButton);

            VBox root = new VBox(tableView, searchLayout);

            Scene scene = new Scene(root, 600, 500);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);

            primaryStage.setTitle("Medication Table");
            primaryStage.show();
        });
    }
    private Medication linearSearch(ObservableList<Medication> data, String searchName) {
        for (Medication med : data) {
            if (med.getName().equalsIgnoreCase(searchName)) {
                return med;
            }
        }
        return null;
    }

    private ObservableList<Medication> bubbleSort(ObservableList<Medication> data) {
        int n = data.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (data.get(i - 1).getName().compareTo(data.get(i).getName()) > 0) {
                    Medication temp = data.get(i - 1);
                    data.set(i - 1, data.get(i));
                    data.set(i, temp);
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
        return data;
    }
}
