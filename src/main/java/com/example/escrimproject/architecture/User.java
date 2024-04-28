package com.example.escrimproject.architecture;

import javafx.beans.property.*;

/**
 * Represents a User in the system with properties for username, password, and role.
 * This class leverages JavaFX properties to facilitate easy data binding and ensure
 * reactive UI updates in a JavaFX application.
 */
public class User {
    private final StringProperty username; // Property for the user's username
    private final StringProperty password; // Property for the user's password
    private final StringProperty role;     // Property for the user's role within the application

    /**
     * Constructor initializes all properties with default values.
     */
    public User() {
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        role = new SimpleStringProperty();
    }

    // Accessors and mutators for username property

    /**
     * Returns the property object for username.
     * @return username property for binding to UI components
     */
    public StringProperty usernameProperty() {
        return username;
    }

    /**
     * Returns the username value.
     * @return the current username as a String
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Sets the username value.
     * @param username the new username as a String
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    // Accessors and mutators for password property

    /**
     * Returns the property object for password.
     * @return password property for binding or security checks
     */
    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * Returns the password value.
     * @return the current password as a String
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Sets the password value.
     * @param password the new password as a String
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    // Accessors and mutators for role property

    /**
     * Returns the property object for role.
     * @return role property for binding to UI components and access control
     */
    public StringProperty roleProperty() {
        return role;
    }

    /**
     * Returns the role value.
     * @return the current role as a String
     */
    public String getRole() {
        return role.get();
    }

    /**
     * Sets the role value.
     * @param role the new role as a String
     */
    public void setRole(String role) {
        this.role.set(role);
    }
}
