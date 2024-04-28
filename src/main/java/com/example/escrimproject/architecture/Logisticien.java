package com.example.escrimproject.architecture;

/**
 * Represents a Logistician, a specific type of personnel within the system.
 * This class extends the general Personnel class, specifying the type attribute for a logistician.
 */
public class Logisticien extends Personnel {

    /**
     * Constructor for the Logistician.
     * Initializes a new instance of a logistician by setting its type via the constructor of the superclass.
     */
    public Logisticien() {
        super(); // Call to the superclass (Personnel) constructor
        setType("Logisticien"); // Explicitly set the type specific to this class
    }
}
