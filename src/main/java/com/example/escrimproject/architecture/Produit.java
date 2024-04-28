package com.example.escrimproject.architecture;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Abstract base class representing a generic product within the system.
 * This class provides common attributes and functionality for various types of products,
 * facilitating property binding and management in a JavaFX application.
 */
public abstract class Produit {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty poids;
    private final StringProperty quantite;
    private final StringProperty type;
    private final StringProperty categoryId;


    /**
     * Constructor initializing properties with default values.
     */
    public Produit() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.poids = new SimpleStringProperty();
        this.quantite = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.categoryId = new SimpleStringProperty();
    }

    // Accessor methods for properties

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty poidsProperty() {
        return poids;
    }

    public String getPoids() {
        return poids.get();
    }

    public void setPoids(float poids) {
        this.poids.set(String.valueOf(poids));
    }

    public StringProperty quantiteProperty() {
        return quantite;
    }

    public String getQuantite() {
        return quantite.get();
    }

    public void setQuantite(int quantite) {
        this.quantite.set(String.valueOf(quantite));
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }
    public void setType(String type) {
        this.type.set(String.valueOf(type));
    }

    public String getCategoryId() {
        return categoryId.get();
    }

    public StringProperty categoryIdProperty() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId.set(String.valueOf(categoryId));
    }

}
