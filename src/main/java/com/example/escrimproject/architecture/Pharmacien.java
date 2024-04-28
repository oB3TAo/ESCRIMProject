package com.example.escrimproject.architecture;

/**
 * Represents a Pharmacist in the system, extending the generic Personnel class.
 * This class specifically sets the personnel type to "Pharmacien", indicating its specialized role.
 */
public class Pharmacien extends Personnel {
    /**
     * Constructor for Pharmacien.
     * Calls the superclass constructor and sets the personnel type to "Pharmacien".
     */
    public Pharmacien() {
        super(); // Call the superclass (Personnel) constructor to ensure proper initialization
        setType("Pharmacien"); // Sets the type specific to this class
    }
}