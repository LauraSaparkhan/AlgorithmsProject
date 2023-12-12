package com.example.algosproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class Medication {
    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty dose = new SimpleStringProperty();
    private final StringProperty time = new SimpleStringProperty();

    public Medication(String name, String dose, String time) {
        this.name.set(name);
        this.dose.set(dose);
        this.time.set(time);
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public String getDose() {
        return dose.get();
    }

    public StringProperty doseProperty() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose.set(dose);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
