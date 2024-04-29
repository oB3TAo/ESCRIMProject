package com.example.escrimproject;

import com.example.escrimproject.architecture.Fournisseur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.*;

public class FournisseurController {

    // FXML elements
    @FXML private TableView<Fournisseur> table; // TableView to display fournisseurs
    @FXML private TableColumn<Fournisseur, Integer> IDFournisseurColmn; // TableColumn for fournisseur IDs
    @FXML private TableColumn<Fournisseur, String> NomColmn; // TableColumn for fournisseur names
    @FXML private TableColumn<Fournisseur, String> AdresseColmn; // TableColumn for fournisseur addresses
    @FXML private TableColumn<Fournisseur, String> TelephoneColmn; // TableColumn for fournisseur telephone numbers
    @FXML private TableColumn<Fournisseur, String> EmailColmn; // TableColumn for fournisseur email addresses
    @FXML private TableColumn<Fournisseur, String> ContactPrincipalColmn; // TableColumn for fournisseur principal contacts

    @FXML private TextField txtNom; // TextField for entering fournisseur name
    @FXML private TextField txtAdresse; // TextField for entering fournisseur address
    @FXML private TextField txtTelephone; // TextField for entering fournisseur telephone number
    @FXML private TextField txtEmail; // TextField for entering fournisseur email address
    @FXML private TextField txtContactPrincipal; // TextField for entering fournisseur principal contact

    private Connection con; // Connection object
    private PreparedStatement pst; // PreparedStatement object
    private ResultSet rs; // ResultSet object

    // Initialize method called after loading the FXML file
    @FXML
    public void initialize() {
        if (areComponentsLoaded()) {
            connectDatabase(); // Establish database connection
            setupCellValueFactories(); // Setup cell value factories for table columns
            loadTableData(); // Load data into the table
            // Listener to populate fields when a row in the table is selected
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Fournisseur fournisseur = table.getSelectionModel().getSelectedItem();
                    txtNom.setText(fournisseur.getNom());
                    txtAdresse.setText(fournisseur.getAdresse());
                    txtTelephone.setText(fournisseur.getTelephone());
                    txtEmail.setText(fournisseur.getEmail());
                    txtContactPrincipal.setText(fournisseur.getContactPrincipal());
                }
            });

        } else {
            System.err.println("UI components are not initialized!"); // Log error if UI components are not initialized
        }
    }

    // Method to check if all UI components are loaded
    private boolean areComponentsLoaded() {
        // Ensure all FX:ID components are not null
        return table != null && IDFournisseurColmn != null && NomColmn != null
                && AdresseColmn != null && TelephoneColmn != null && EmailColmn != null
                && ContactPrincipalColmn != null;
    }

    // Method to establish a connection to the database
    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Loading the MySQL JDBC driver
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90"); // Establishing connection to the database
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage()); // Handling connection errors
        }
    }

    // Method to handle logout action
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            // Get the current stage from the action event triggered by the button
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the login scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log any errors encountered while loading the login view
        }
    }

    // Setup cell value factories for table columns
    private void setupCellValueFactories() {
        IDFournisseurColmn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        NomColmn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        AdresseColmn.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
        TelephoneColmn.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
        EmailColmn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        ContactPrincipalColmn.setCellValueFactory(cellData -> cellData.getValue().contactPrincipalProperty());
    }

    // Load data into the table
    private void loadTableData() {
        ObservableList<Fournisseur> fournisseurs = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Fournisseur"); // Preparing SQL statement to select all fournisseurs
            rs = pst.executeQuery(); // Executing the query
            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(); // Creating a new Fournisseur object
                fournisseur.setId(rs.getInt("ID_Fournisseur")); // Setting the fournisseur ID
                fournisseur.setNom(rs.getString("Nom")); // Setting the fournisseur name
                fournisseur.setAdresse(rs.getString("Adresse")); // Setting the fournisseur address
                fournisseur.setTelephone(rs.getString("Telephone")); // Setting the fournisseur telephone number
                fournisseur.setEmail(rs.getString("Email")); // Setting the fournisseur email address
                fournisseur.setContactPrincipal(rs.getString("Contact_Principal")); // Setting the fournisseur principal contact
                fournisseurs.add(fournisseur); // Adding the Fournisseur object to the ObservableList
            }
            table.setItems(fournisseurs); // Setting the ObservableList as the items of the TableView
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage()); // Handling data loading errors
        }
    }

    // Add fournisseur button action
    @FXML
    private void Add(ActionEvent event) {
        String sql = "INSERT INTO Fournisseur (Nom, Adresse, Telephone, Email, Contact_Principal) VALUES (?, ?, ?, ?, ?)";
        try {
            pst = con.prepareStatement(sql); // Preparing the SQL statement
            pst.setString(1, txtNom.getText()); // Setting the fournisseur name parameter
            pst.setString(2, txtAdresse.getText()); // Setting the fournisseur address parameter
            pst.setString(3, txtTelephone.getText()); // Setting the fournisseur telephone number parameter
            pst.setString(4, txtEmail.getText()); // Setting the fournisseur email address parameter
            pst.setString(5, txtContactPrincipal.getText()); // Setting the fournisseur principal contact parameter
            int affectedRows = pst.executeUpdate(); // Executing the insertion query
            // Show appropriate message based on the result of the insertion
            if (affectedRows > 0) {
                System.out.println("Fournisseur added successfully."); // Showing success message
                loadTableData(); // Reloading table data
            } else {
                System.out.println("No fournisseur was added."); // Showing failure message
            }
        } catch (SQLException e) {
            System.err.println("Failed to add fournisseur: " + e.getMessage()); // Handling SQL errors
        }
    }

    // Update fournisseur button action
    @FXML
    void Update(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Fournisseur selectedFournisseur = table.getSelectionModel().getSelectedItem(); // Getting the selected fournisseur
            String sql = "UPDATE Fournisseur SET Nom=?, Adresse=?, Telephone=?, Email=?, Contact_Principal=? WHERE ID_Fournisseur=?";
            try {
                pst = con.prepareStatement(sql); // Preparing the SQL statement
                pst.setString(1, txtNom.getText()); // Setting the fournisseur name parameter
                pst.setString(2, txtAdresse.getText()); // Setting the fournisseur address parameter
                pst.setString(3, txtTelephone.getText()); // Setting the fournisseur telephone number parameter
                pst.setString(4, txtEmail.getText()); // Setting the fournisseur email address parameter
                pst.setString(5, txtContactPrincipal.getText()); // Setting the fournisseur principal contact parameter
                pst.setInt(6, selectedFournisseur.getId()); // Setting the fournisseur ID parameter
                int affectedRows = pst.executeUpdate(); // Executing the update query
                // Show appropriate message based on the result of the update
                if (affectedRows > 0) {
                    System.out.println("Fournisseur updated successfully."); // Showing success message
                    loadTableData(); // Reloading table data
                } else {
                    System.out.println("No fournisseur was updated."); // Showing failure message
                }
            } catch (SQLException e) {
                System.err.println("Failed to update fournisseur: " + e.getMessage()); // Handling SQL errors
            }
        } else {
            System.out.println("No fournisseur selected for update."); // Showing an alert if no fournisseur is selected
        }
    }

    // Delete fournisseur button action
    @FXML
    void Delete(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Fournisseur selectedFournisseur = table.getSelectionModel().getSelectedItem(); // Getting the selected fournisseur
            String sql = "DELETE FROM Fournisseur WHERE ID_Fournisseur=?";
            try {
                pst = con.prepareStatement(sql); // Preparing the SQL statement
                pst.setInt(1, selectedFournisseur.getId()); // Setting the fournisseur ID parameter
                int affectedRows = pst.executeUpdate(); // Executing the deletion query
                // Show appropriate message based on the result of the deletion
                if (affectedRows > 0) {
                    System.out.println("Fournisseur deleted successfully."); // Showing success message
                    loadTableData(); // Reloading table data
                } else {
                    System.out.println("No fournisseur was deleted."); // Showing failure message
                }
            } catch (SQLException e) {
                System.err.println("Failed to delete fournisseur: " + e.getMessage()); // Handling SQL errors
            }
        } else {
            System.out.println("No fournisseur selected for deletion."); // Showing an alert if no fournisseur is selected
        }
    }
}
