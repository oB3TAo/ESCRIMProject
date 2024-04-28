package com.example.escrimproject.architecture;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Represents a command or order within the system with properties that are bindable in a JavaFX UI.
 * This class uses JavaFX properties to enable automatic updates between UI components and the model,
 * facilitating an interactive and responsive user interface.
 */
public class Commande {
    // JavaFX properties for the Commande class
    private final IntegerProperty idCommande;
    private final ObjectProperty<LocalDate> dateCommande;
    private final ObjectProperty<LocalDate> dateLivraisonPrevue;
    private final StringProperty statutCommande;
    private final DoubleProperty montantTotal;
    private final IntegerProperty idFournisseur;
    private final IntegerProperty idPersonnel;
    private final IntegerProperty idProduit;

    /**
     * Constructor for Commande initializes all properties with default values.
     */
    public Commande() {
        this.idCommande = new SimpleIntegerProperty();
        this.dateCommande = new SimpleObjectProperty<>();
        this.dateLivraisonPrevue = new SimpleObjectProperty<>();
        this.statutCommande = new SimpleStringProperty();
        this.montantTotal = new SimpleDoubleProperty();
        this.idFournisseur = new SimpleIntegerProperty();
        this.idPersonnel = new SimpleIntegerProperty();
        this.idProduit = new SimpleIntegerProperty();
    }

    // Property getters provide access to the property instances.

    public IntegerProperty idCommandeProperty() {
        return idCommande;
    }

    public ObjectProperty<LocalDate> dateCommandeProperty() {
        return dateCommande;
    }

    public ObjectProperty<LocalDate> dateLivraisonPrevueProperty() {
        return dateLivraisonPrevue;
    }

    public StringProperty statutCommandeProperty() {
        return statutCommande;
    }

    public DoubleProperty montantTotalProperty() {
        return montantTotal;
    }

    public IntegerProperty idFournisseurProperty() {
        return idFournisseur;
    }

    public IntegerProperty idPersonnelProperty() {
        return idPersonnel;
    }

    public IntegerProperty idProduitProperty() {
        return idProduit;
    }

    // Standard getters and setters for each property.

    public int getIdCommande() {
        return idCommande.get();
    }

    public void setIdCommande(int idCommande) {
        this.idCommande.set(idCommande);
    }

    public LocalDate getDateCommande() {
        return dateCommande.get();
    }

    public void setDateCommande(LocalDate date) {
        this.dateCommande.set(date);
    }

    public LocalDate getDateLivraisonPrevue() {
        return dateLivraisonPrevue.get();
    }

    public void setDateLivraisonPrevue(LocalDate date) {
        this.dateLivraisonPrevue.set(date);
    }

    public String getStatutCommande() {
        return statutCommande.get();
    }

    public void setStatutCommande(String statut) {
        this.statutCommande.set(statut);
    }

    public double getMontantTotal() {
        return montantTotal.get();
    }

    public void setMontantTotal(double montant) {
        this.montantTotal.set(montant);
    }

    public int getIdFournisseur() {
        return idFournisseur.get();
    }

    public void setIdFournisseur(int id) {
        this.idFournisseur.set(id);
    }

    public int getIdPersonnel() {
        return idPersonnel.get();
    }

    public void setIdPersonnel(int id) {
        this.idPersonnel.set(id);
    }

    public int getIdProduit() {
        return idProduit.get();
    }

    public void setIdProduit(int id) {
        this.idProduit.set(id);
    }
}
