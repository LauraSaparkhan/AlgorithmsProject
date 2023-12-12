module com.example.algosproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.algosproject to javafx.fxml;
    exports com.example.algosproject;
}