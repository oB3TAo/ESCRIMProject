package com.example.escrimproject.architecture;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Produit {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty poids;
    private final StringProperty quantite;

    public Produit() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.poids = new SimpleStringProperty();
        this.quantite = new SimpleStringProperty();
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

    public void setQuantite(float quantite) {
        this.quantite.set(String.valueOf(quantite));
    }
}
