package com.example.escrimproject.architecture;

public class Patient {
    private int identifiant;
    private String nom;
    private String diagnostic;
    private String traitementEnCours;
    private boolean isAlive;
    private boolean isTakenInCharge;

    // Default constructor
    public Patient() {
    }

    // Parameterized constructor
    public Patient(int identifiant, String nom, String diagnostic, String traitementEnCours, boolean isAlive, boolean isTakenInCharge) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.diagnostic = diagnostic;
        this.traitementEnCours = traitementEnCours;
        this.isAlive = isAlive;
        this.isTakenInCharge = isTakenInCharge;
    }

    // Getters and setters
    public int getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant = identifiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getTraitementEnCours() {
        return traitementEnCours;
    }

    public void setTraitementEnCours(String traitementEnCours) {
        this.traitementEnCours = traitementEnCours;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isTakenInCharge() {
        return isTakenInCharge;
    }

    public void setTakenInCharge(boolean isTakenInCharge) {
        this.isTakenInCharge = isTakenInCharge;
    }
}
