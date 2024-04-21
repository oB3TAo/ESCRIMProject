package com.example.escrimproject;

import com.example.escrimproject.architecture.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController implements Initializable {

    @FXML
    private TableColumn<?, ?> IdPersColmn;

    @FXML
    private TableColumn<?, ?> PasswordColmn;

    @FXML
    private TableColumn<?, ?> TypeColmn;

    @FXML
    private TableColumn<?, ?> UsernameColmn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbIdPers;

    @FXML
    private TableView<User> table;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRole;

    @FXML
    private TextField txtUsername;

    private Connection con;
    private PreparedStatement pst;

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        if (table!=null){
            // Populate the table with data from the database
            table();
            // Initialize ComboBox with personnel IDs
            initializeComboBox();
        }else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }


    }

    private void initializeComboBox() {
        ObservableList<String> personnelIds = FXCollections.observableArrayList();
        try {
            String sql = "SELECT ID_Personnel, Nom FROM Personnel";
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID_Personnel");
                String name = rs.getString("Nom");
                personnelIds.add(id + " - " + name);
            }
            cmbIdPers.setItems(personnelIds);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    void Add(ActionEvent event) {
        // Get selected personnel ID from ComboBox
        String selectedPersonnel = cmbIdPers.getValue();
        if (selectedPersonnel == null) {
            showAlert("Please select a personnel ID.");
            return;
        }
        // Extract personnel ID from selected value
        String[] parts = selectedPersonnel.split(" - ");
        String personnelId = parts[0];

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = txtRole.getText();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        try {
            String sql = "INSERT INTO User (username, password, role, ID_Personnel) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);
            pst.setString(4, personnelId);
            pst.executeUpdate();

            showAlert("User Added!");
            table();

            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        User selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Please select a user to delete.");
            return;
        }
        String username = selectedItem.getUsername();

        try {
            String sql = "DELETE FROM User WHERE username = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.executeUpdate();

            showAlert("User Deleted!");
            table();
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    void Update(ActionEvent event) {
        User selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Please select a user to update.");
            return;
        }
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = txtRole.getText();

        try {
            String sql = "UPDATE User SET password = ?, role = ? WHERE username = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, password);
            pst.setString(2, role);
            pst.setString(3, username);
            pst.executeUpdate();

            showAlert("User Updated!");
            table();
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User");
        alert.setHeaderText("User");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        txtRole.clear();
    }

    public void table() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM User";
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                User user = new User(username, password, role);
                users.add(user);
            }
            table.setItems(users);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

}
