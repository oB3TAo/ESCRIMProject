package com.example.escrimproject.architecture;

import javafx.beans.property.*;

import java.time.LocalDate;


/**
 * Represents a Patient in the healthcare system with detailed personal and medical information.
 * This class leverages JavaFX properties to facilitate easy binding with UI components and ensure
 * reactive updates in a JavaFX application.
 */
public class Patient {
    // JavaFX properties for the patient's details
    private final IntegerProperty id;
    private final StringProperty nom;
    private final ObjectProperty<LocalDate> dateDeNaissance;
    private final StringProperty sexe;
    private final StringProperty numeroSecuriteSocial;
    private final StringProperty adresse;
    private final StringProperty numeroTelephone;
    private final StringProperty email;
    private final StringProperty traitementEnCours;
    private final StringProperty diagnostic;
    private final StringProperty statut;
    private final IntegerProperty idPersonnel;

    /**
     * Constructor that initializes all JavaFX properties to their default state.
     */
    public Patient() {
        id = new SimpleIntegerProperty();
        nom = new SimpleStringProperty();
        dateDeNaissance = new SimpleObjectProperty<>();
        sexe = new SimpleStringProperty();
        numeroSecuriteSocial = new SimpleStringProperty();
        adresse = new SimpleStringProperty();
        numeroTelephone = new SimpleStringProperty();
        email = new SimpleStringProperty();
        traitementEnCours = new SimpleStringProperty();
        diagnostic = new SimpleStringProperty();
        statut = new SimpleStringProperty();
        idPersonnel = new SimpleIntegerProperty();
    }

    // Property accessors to facilitate data binding and manipulation in JavaFX UI components

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

    public ObjectProperty<LocalDate> dateDeNaissanceProperty() {
        return dateDeNaissance;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance.get();
    }

    public void setDateDeNaissance(LocalDate newDateDeNaissance) {
        dateDeNaissance.set(newDateDeNaissance);
    }

    public StringProperty sexeProperty() {
        return sexe;
    }

    public String getSexe() {
        return sexe.get();
    }

    public void setSexe(String newSexe) {
        sexe.set(newSexe);
    }

    public StringProperty numeroSecuriteSocialProperty() {
        return numeroSecuriteSocial;
    }

    public String getNumeroSecuriteSocial() {
        return numeroSecuriteSocial.get();
    }

    public void setNumeroSecuriteSocial(String newNumeroSecuriteSocial) {
        numeroSecuriteSocial.set(newNumeroSecuriteSocial);
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public String getAdresse() {
        return adresse.get();
    }

    public void setAdresse(String newAdresse) {
        adresse.set(newAdresse);
    }

    public StringProperty numeroTelephoneProperty() {
        return numeroTelephone;
    }

    public String getNumeroTelephone() {
        return numeroTelephone.get();
    }

    public void setNumeroTelephone(String newNumeroTelephone) {
        numeroTelephone.set(newNumeroTelephone);
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

    public StringProperty traitementEnCoursProperty() {
        return traitementEnCours;
    }

    public String getTraitementEnCours() {
        return traitementEnCours.get();
    }

    public void setTraitementEnCours(String newTraitementEnCours) {
        traitementEnCours.set(newTraitementEnCours);
    }

    public StringProperty diagnosticProperty() {
        return diagnostic;
    }

    public String getDiagnostic() {
        return diagnostic.get();
    }

    public void setDiagnostic(String newDiagnostic) {
        diagnostic.set(newDiagnostic);
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

    public IntegerProperty idPersonnelProperty() {
        return idPersonnel;
    }

    public int getIdPersonnel() {
        return idPersonnel.get();
    }

    public void setIdPersonnel(int newIdPersonnel) {
        idPersonnel.set(newIdPersonnel);
    }
}
