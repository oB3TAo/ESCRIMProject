package com.example.escrimproject.architecture;

import java.util.Date;

/**
 * Represents a Medication product with an expiration date.
 * Extends 'Produit' by adding a property for the expiration date of the medication.
 */
public class Medicament extends Produit {

    private Date dateDePeremption;

    /**
     * Gets the expiration date as a Date.
     * @return the Date value of the expiration date.
     */
    public String getDateDePeremption() {
        return String.valueOf(dateDePeremption);
    }

    /**
     * Sets the expiration date from a Date.
     * @param dateDePeremption the Date to set as the expiration date.
     */
    public void setDateDePeremption(Date dateDePeremption) {
        this.dateDePeremption = dateDePeremption;
    }

}

