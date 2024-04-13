package com.example.escrimproject.architecture;

import java.util.Date;

public class Medicament extends Produit {
    private Date dateDePeremption;

    public Date getDateDePeremption() {
        return dateDePeremption;
    }

    public void setDateDePeremption(Date dateDePeremption) {
        this.dateDePeremption = dateDePeremption;
    }
    public Date dateDePeremptionProperty() {
        return dateDePeremption;
    }
}

