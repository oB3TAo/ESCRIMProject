package com.example.escrimproject.architecture;

import java.time.LocalDate;

public class Commande {
    private int numeroCommande;
    private LocalDate dateCommande; // Assuming you want to use a proper date type
    private String etat;

    // Default constructor
    public Commande() {
    }

    // Parameterized constructor
    public Commande(int numeroCommande, LocalDate dateCommande, String etat) {
        this.numeroCommande = numeroCommande;
        this.dateCommande = dateCommande;
        this.etat = etat;
    }

    // Getters and setters
    public int getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(int numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}

