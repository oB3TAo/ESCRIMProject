package com.example.escrimproject.architecture;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Represents a Category with properties bindable in a JavaFX UI.
 * This class utilizes JavaFX properties to enable easy binding and updating between the UI and the model.
 */
public class Category {
    // Properties to hold the category ID, name, and description
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;

    /**
     * Constructor for Category, initializes properties with default values.
     */
    public Category() {
        this.id = new SimpleIntegerProperty(); // Initializes the ID with a default integer property (0)
        this.name = new SimpleStringProperty(); // Initializes the name with a default string property (null)
        this.description = new SimpleStringProperty(); // Initializes the description with a default string property (null)
    }

    /**
     * Returns the IntegerProperty for id.
     * @return the property object for the ID, suitable for binding to UI components.
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Returns the category ID value.
     * @return the integer value of id.
     */
    public int getId() {
        return id.get();
    }

    /**
     * Sets the category ID.
     * @param id the integer to set as the category ID.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Returns the StringProperty for name.
     * @return the property object for the name, suitable for binding to UI components.
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Returns the category name value.
     * @return the string value of the name.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Sets the category name.
     * @param name the string to set as the category name.
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Returns the StringProperty for description.
     * @return the property object for the description, suitable for binding to UI components.
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Returns the category description value.
     * @return the string value of the description.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the category description.
     * @param description the string to set as the category description.
     */
    public void setDescription(String description) {
        this.description.set(description);
    }
}
