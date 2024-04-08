package com.example.escrimproject.architecture;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Medecin extends Personnel {

    private final StringProperty specialite;

    public Medecin() {
        super();
        setType("Medecin");
        specialite = new SimpleStringProperty();
    }

    public String getSpecialite() {
        return specialite.get();
    }

    public StringProperty specialiteProperty() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite.set(specialite);
    }
}
