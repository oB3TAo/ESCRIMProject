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
            // Get the role of the user
            String role = getUserRole(username);

            // Load the appropriate tab based on the user's role
            if ("Medecin".equals(role)) {
                loadMedecinTab();
            } else if ("Logisticien".equals(role)) {
                loadLogisticienTab();
            } else if ("Admin".equals(role)) {
                loadAdminTab();
            } else {
                // Display an error message if the role is not recognized
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Role");
                alert.setContentText("Your role is not recognized.");
                alert.showAndWait();
            }
        } else {
            // Display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid username or password");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    private String getUserRole(String username) throws SQLException {
        String query = "SELECT Role FROM User WHERE Username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("Role");
        } else {
            return null; // User not found
        }
    }

    private void loadMedecinTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("medecin_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            // Pass any necessary data to the controller of the medecin tab if needed
            // MedecinTabController controller = loader.getController();
            // controller.initData(...);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLogisticienTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logisticien_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 688, 470));
            // Pass any necessary data to the controller of the logisticien tab if needed
            // LogisticienTabController controller = loader.getController();
            // controller.initData(...);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 802, 517));
            // Pass any necessary data to the controller of the admin tab if needed
            // AdminController controller = loader.getController();
            // controller.initData(...);
        } catch (IOException e) {
            e.printStackTrace();
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
