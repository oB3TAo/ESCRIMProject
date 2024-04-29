package com.example.escrimproject;

import com.example.escrimproject.architecture.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    // FXML elements
    @FXML
    private TextField usernameField; // TextField for entering username
    @FXML
    private PasswordField passwordField; // PasswordField for entering password

    private Connection connection; // Connection object for database access

    // Initialize method called after loading the FXML file
    @FXML
    private void initialize() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (SQLException e) {
            e.printStackTrace(); // Log any errors encountered during database connection
        }
    }

    // Handle login button click event
    @FXML
    private void handleLoginButtonClick(ActionEvent event) throws SQLException {
        String username = usernameField.getText(); // Get username from text field
        String password = passwordField.getText(); // Get password from password field

        // Check if the username and password are valid
        if (isValidUser(username, password)) {
            // Get the role of the user
            String role = getUserRole(username);

            // Load the appropriate tab based on the user's role
            switch (role) {
                case "Medecin":
                    loadMedecinTab();
                    break;
                case "Logisticien":
                    loadLogisticienTab();
                    break;
                case "Pharmacien":
                    loadPharmacienTab();
                    break;
                case "Admin":
                    loadAdminTab();
                    break;
                default:
                    // Display an error message if the role is not recognized
                    showAlert(Alert.AlertType.ERROR, "Invalid Role", "Your role is not recognized.");
                    break;
            }
        } else {
            // Display an error message for invalid username or password
            showAlert(Alert.AlertType.ERROR, "Invalid username or password", "Please try again.");
        }
    }

    // Method to get the role of the user from the database
    private String getUserRole(String username) throws SQLException {
        String query = "SELECT Role FROM User WHERE Username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("Role"); // Return the role if found
        } else {
            return null; // Return null if user not found
        }
    }

    // Method to load the medecin tab
    private void loadMedecinTab() {
        loadTab("medecin_view.fxml", 900, 600);
    }

    // Method to load the logisticien tab
    private void loadLogisticienTab() {
        loadTab("logisticien_view.fxml", 900, 530);
    }

    // Method to load the pharmacien tab
    private void loadPharmacienTab() {
        loadTab("pharmacien_view.fxml", 700, 507);
    }

    // Method to load the admin tab
    private void loadAdminTab() {
        loadTab("admin_view.fxml", 802, 530);
    }

    // Method to load a specific tab
    private void loadTab(String fxmlPath, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
        } catch (IOException e) {
            e.printStackTrace(); // Log any errors encountered while loading the tab
        }
    }

    // Method to check if the username and password are valid
    private boolean isValidUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String storedPassword = resultSet.getString("Password");
            return storedPassword.equals(password); // Return true if passwords match
        } else {
            return false; // Return false if user not found
        }
    }

    // Method to show an alert dialog
    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
