package com.example.escrimproject.architecture;

import javafx.beans.property.*;

/**
 * Abstract class representing a general personnel entity in the system.
 * This class is designed to be extended by specific types of personnel, such as Medecin or Logisticien.
 * It includes common attributes like id, name, email, telephone, status, and type,
 * which are fundamental to all personnel types.
 */
public abstract class Personnel {
    // Properties for the common attributes of personnel
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty email;
    private final StringProperty telephone;
    private final StringProperty statut;
    private final StringProperty type;

    /**
     * Constructor initializes all JavaFX properties.
     */
    public Personnel() {
        id = new SimpleIntegerProperty();
        nom = new SimpleStringProperty();
        email = new SimpleStringProperty();
        telephone = new SimpleStringProperty();
        statut = new SimpleStringProperty();
        type = new SimpleStringProperty();
    }

    // Property accessors and getters/setters for each attribute

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int newId) {
        id.set(newId);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String newNom) {
        nom.set(newNom);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String newEmail) {
        email.set(newEmail);
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String newTelephone) {
        telephone.set(newTelephone);
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String newStatut) {
        statut.set(newStatut);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }
    public void setType(String newType) {
        type.set(newType);
    }

}
