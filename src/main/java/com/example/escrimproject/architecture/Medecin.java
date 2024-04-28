package com.example.escrimproject.architecture;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Represents a medical doctor (Medecin) within the system.
 * Extends the generic Personnel class by adding a specialization (specialite) property,
 * which allows the tracking of the doctor's medical specialty.
 */
public class Medecin extends Personnel {

    // Property to hold the medical specialty of the doctor
    private final StringProperty specialite;

    /**
     * Constructor for Medecin.
     * Initializes the specialty property and sets the type specific to this subclass.
     */
    public Medecin() {
        super(); // Calls the constructor of the superclass (Personnel) to ensure proper initialization
        setType("Medecin"); // Sets the type of personnel to "Medecin"
        specialite = new SimpleStringProperty(); // Initializes the specialite property with a default value
    }

    /**
     * Gets the medical specialty of the Medecin.
     * @return A string representing the medical specialty.
     */
    public String getSpecialite() {

        return specialite.get();
    }

    /**
     * Returns the medical specialty as a StringProperty.
     * This property allows for binding with UI elements in a JavaFX application, facilitating real-time updates.
     * @return StringProperty for binding the medical specialty in the UI.
     */

    public StringProperty specialiteProperty() {

        return specialite;
    }

    /**
     * Sets the medical specialty of the Medecin.
     * @param specialite A string to set as the new medical specialty.
     */
    public void setSpecialite(String specialite) {
        this.specialite.set(specialite);
    }
}
