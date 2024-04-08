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

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Connection connection;

    @FXML
    private void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButtonClick(ActionEvent event) throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the username and password are valid
        if (isValidUser(username, password)) {
            // Load the main view
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("main_tab.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 688 , 470));
        } else {
            // Display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid username or password");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    private boolean isValidUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String storedPassword = resultSet.getString("Password");
            return storedPassword.equals(password);
        } else {
            return false;
        }
    }
}
