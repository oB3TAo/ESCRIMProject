package com.example.escrimproject.architecture;

import javafx.beans.property.*;


/**
 * Represents a Supplier entity in the system with properties that are bindable in a JavaFX UI.
 * This class is designed to interact closely with JavaFX components through property bindings,
 * facilitating dynamic updates and interactions within the user interface.
 */
public class Fournisseur {
    // Properties to hold supplier details
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty adresse;
    private final StringProperty telephone;
    private final StringProperty email;
    private final StringProperty contactPrincipal;

    /**
     * Default constructor initializing all properties.
     */
    public Fournisseur() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.adresse = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.contactPrincipal = new SimpleStringProperty();
    }

    // Property accessors to facilitate data binding and manipulation in JavaFX UI components

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public StringProperty telephoneProperty() {
        return telephone;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty contactPrincipalProperty() {
        return contactPrincipal;
    }

    // Standard getters and setters for the properties

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String Newnom) {
        nom.set(Newnom);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String Newadresse) {
        adresse.set(Newadresse);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String Newtelephone) {
        telephone.set(Newtelephone);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String Newemail) {
        email.set(Newemail);
    }

    public String getContactPrincipal() {
        return contactPrincipal.get();
    }

    public void setContactPrincipal(String NewcontactPrincipal) {
        contactPrincipal.set(NewcontactPrincipal);
    }
}
