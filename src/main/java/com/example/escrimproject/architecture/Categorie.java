package com.example.escrimproject.architecture;

public class Categorie {
    private int id_Categorie;
    private String nom_Categorie;
    private String description;

    public int getId_Categorie() {
        return id_Categorie;
    }

    public void setId_Categorie(int id_Categorie) {
        this.id_Categorie = id_Categorie;
    }

    public String getNom_Categorie() {
        return nom_Categorie;
    }

    public void setNom_Categorie(String nom_Categorie) {
        this.nom_Categorie = nom_Categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
