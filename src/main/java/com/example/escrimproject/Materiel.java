package com.example.escrimproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Materiel {
    private final StringProperty id;
    private final StringProperty nom;
    private final StringProperty categorie;
    private final StringProperty quantite;
    private final StringProperty dateAchat;
    private final StringProperty derniereVerification;
    private final StringProperty datePeremption;
    private final StringProperty fournisseur;
    private final StringProperty cout;

    public Materiel() {
        id = new SimpleStringProperty(this, "id");
        nom = new SimpleStringProperty(this, "nom");
        categorie = new SimpleStringProperty(this, "categorie");
        quantite = new SimpleStringProperty(this, "quantite");
        dateAchat = new SimpleStringProperty(this, "dateAchat");
        derniereVerification = new SimpleStringProperty(this, "derniereVerification");
        datePeremption = new SimpleStringProperty(this, "datePeremption");
        fournisseur = new SimpleStringProperty(this, "fournisseur");
        cout = new SimpleStringProperty(this, "cout");
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String newId) {
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

    public StringProperty categorieProperty() {
        return categorie;
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String newCategorie) {
        categorie.set(newCategorie);
    }

    public StringProperty quantiteProperty() {
        return quantite;
    }

    public String getQuantite() {
        return quantite.get();
    }

    public void setQuantite(String newQuantite) {
        quantite.set(newQuantite);
    }

    public StringProperty dateAchatProperty() {
        return dateAchat;
    }

    public String getDateAchat() {
        return dateAchat.get();
    }

    public void setDateAchat(String newDateAchat) {
        dateAchat.set(newDateAchat);
    }

    public StringProperty derniereVerificationProperty() {
        return derniereVerification;
    }

    public String getDerniereVerification() {
        return derniereVerification.get();
    }

    public void setDerniereVerification(String newDerniereVerification) {
        derniereVerification.set(newDerniereVerification);
    }

    public StringProperty datePeremptionProperty() {
        return datePeremption;
    }

    public String getDatePeremption() {
        return datePeremption.get();
    }

    public void setDatePeremption(String newDatePeremption) {
        datePeremption.set(newDatePeremption);
    }

    public StringProperty fournisseurProperty() {
        return fournisseur;
    }

    public String getFournisseur() {
        return fournisseur.get();
    }

    public void setFournisseur(String newFournisseur) {
        fournisseur.set(newFournisseur);
    }

    public StringProperty coutProperty() {
        return cout;
    }

    public String getCout() {
        return cout.get();
    }

    public void setCout(String newCout) {
        cout.set(newCout);
    }
}

