package com.example.escrimproject.architecture;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Produit {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final FloatProperty poids;
    private final FloatProperty quantite;

    public Produit() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.poids = new SimpleFloatProperty();
        this.quantite = new SimpleFloatProperty();
    }

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

    public FloatProperty poidsProperty() {
        return poids;
    }

    public float getPoids() {
        return poids.get();
    }

    public void setPoids(float poids) {
        this.poids.set(poids);
    }

    public FloatProperty quantiteProperty() {
        return quantite;
    }

    public float getQuantite() {
        return quantite.get();
    }

    public void setQuantite(float quantite) {
        this.quantite.set(quantite);
    }
}
