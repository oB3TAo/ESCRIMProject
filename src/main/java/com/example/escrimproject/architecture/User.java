package com.example.escrimproject.architecture;

import javafx.beans.property.*;

public class User {
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty role;

    // Constructor
    public User() {
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        role = new SimpleStringProperty();
    }

    // Username property
    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    // Password property
    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Role property
    public StringProperty roleProperty() {
        return role;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }
}
