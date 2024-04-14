package com.example.escrimproject.architecture;

import java.time.LocalDate;
import java.util.Date;

public class Medicament extends Produit {
    private Date dateDePeremption;

    public String getDateDePeremption() {
        return String.valueOf(dateDePeremption);
    }

    public void setDateDePeremption(Date dateDePeremption) {
        this.dateDePeremption = dateDePeremption;
    }
    public Date dateDePeremptionProperty() {
        return dateDePeremption;
    }
}

