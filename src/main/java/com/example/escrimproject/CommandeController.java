package com.example.escrimproject;

import com.example.escrimproject.architecture.Commande; // Importing the Commande class
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class CommandeController {

    // FXML elements
    @FXML private TableView<Commande> table; // TableView to display commandes
    @FXML private TableColumn<Commande, Integer> idCommandeCol; // TableColumn for commande IDs
    @FXML private TableColumn<Commande, LocalDate> dateCommandeCol; // TableColumn for commande dates
    @FXML private TableColumn<Commande, LocalDate> dateLivraisonPrevueCol; // TableColumn for expected delivery dates
    @FXML private TableColumn<Commande, String> statutCommandeCol; // TableColumn for commande statuses
    @FXML private TableColumn<Commande, Double> montantTotalCol; // TableColumn for total amounts
    @FXML private TableColumn<Commande, Integer> idFournisseurCol; // TableColumn for supplier IDs
    @FXML private TableColumn<Commande, Integer> idPersonnelCol; // TableColumn for personnel IDs
    @FXML private TableColumn<Commande, Integer> idProduitCol; // TableColumn for product IDs

    @FXML private DatePicker txtDateCommande; // DatePicker for entering commande date
    @FXML private DatePicker txtDateLivraisonPrevue; // DatePicker for entering expected delivery date
    @FXML private TextField txtStatutCommande; // TextField for entering commande status
    @FXML private TextField txtMontantTotal; // TextField for entering total amount
    @FXML private TextField txtIdFournisseur; // TextField for entering supplier ID
    @FXML private TextField txtIdPersonnel; // TextField for entering personnel ID
    @FXML private TextField txtIdProduit; // TextField for entering product ID

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
                    Commande commande = table.getSelectionModel().getSelectedItem();
                    txtDateCommande.setValue(commande.getDateCommande());
                    txtDateLivraisonPrevue.setValue(commande.getDateLivraisonPrevue());
                    txtStatutCommande.setText(commande.getStatutCommande());
                    txtMontantTotal.setText(String.valueOf(commande.getMontantTotal()));
                    txtIdFournisseur.setText(String.valueOf(commande.getIdFournisseur()));
                    txtIdPersonnel.setText(String.valueOf(commande.getIdPersonnel()));
                    txtIdProduit.setText(String.valueOf(commande.getIdProduit()));
                }
            });

        } else {
            System.err.println("UI components are not initialized!"); // Log error if UI components are not initialized
        }
    }

    // Method to check if all UI components are loaded
    private boolean areComponentsLoaded() {
        return table != null && idCommandeCol != null && dateCommandeCol != null
                && dateLivraisonPrevueCol != null && statutCommandeCol != null
                && montantTotalCol != null && idFournisseurCol != null
                && idPersonnelCol != null && idProduitCol != null;
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
        idCommandeCol.setCellValueFactory(cellData -> cellData.getValue().idCommandeProperty().asObject());
        dateCommandeCol.setCellValueFactory(cellData -> cellData.getValue().dateCommandeProperty());
        dateLivraisonPrevueCol.setCellValueFactory(cellData -> cellData.getValue().dateLivraisonPrevueProperty());
        statutCommandeCol.setCellValueFactory(cellData -> cellData.getValue().statutCommandeProperty());
        montantTotalCol.setCellValueFactory(cellData -> cellData.getValue().montantTotalProperty().asObject());
        idFournisseurCol.setCellValueFactory(cellData -> cellData.getValue().idFournisseurProperty().asObject());
        idPersonnelCol.setCellValueFactory(cellData -> cellData.getValue().idPersonnelProperty().asObject());
        idProduitCol.setCellValueFactory(cellData -> cellData.getValue().idProduitProperty().asObject());
    }

    // Load data into the table
    private void loadTableData() {
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Commande"); // Preparing SQL statement to select all commandes
            rs = pst.executeQuery(); // Executing the query
            while (rs.next()) {
                Commande commande = new Commande(); // Creating a new Commande object
                commande.setIdCommande(rs.getInt("ID_Commande")); // Setting the commande ID
                commande.setDateCommande(rs.getDate("Date_Commande").toLocalDate()); // Setting the commande date
                commande.setDateLivraisonPrevue(rs.getDate("Date_Livraison_Prevue").toLocalDate()); // Setting the expected delivery date
                commande.setStatutCommande(rs.getString("Statut_Commande")); // Setting the commande status
                commande.setMontantTotal(rs.getDouble("Montant_Total")); // Setting the total amount
                commande.setIdFournisseur(rs.getInt("ID_Fournisseur")); // Setting the supplier ID
                commande.setIdPersonnel(rs.getInt("ID_Personnel")); // Setting the personnel ID
                commande.setIdProduit(rs.getInt("ID_Produit")); // Setting the product ID
                commandes.add(commande); // Adding the Commande object to the ObservableList
            }
            table.setItems(commandes); // Setting the ObservableList as the items of the TableView
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage()); // Handling data loading errors
        }
    }

    // Add commande button action
    @FXML
    private void Add(ActionEvent event) {
        String sql = "INSERT INTO Commande (Date_Commande, Date_Livraison_Prevue, Statut_Commande, Montant_Total, ID_Fournisseur, ID_Personnel, ID_Produit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = con.prepareStatement(sql); // Preparing the SQL statement
            pst.setDate(1, Date.valueOf(txtDateCommande.getValue())); // Setting the commande date parameter
            pst.setDate(2, Date.valueOf(txtDateLivraisonPrevue.getValue())); // Setting the expected delivery date parameter
            pst.setString(3, txtStatutCommande.getText()); // Setting the commande status parameter
            pst.setDouble(4, Double.parseDouble(txtMontantTotal.getText())); // Setting the total amount parameter
            pst.setInt(5, Integer.parseInt(txtIdFournisseur.getText())); // Setting the supplier ID parameter
            pst.setInt(6, Integer.parseInt(txtIdPersonnel.getText())); // Setting the personnel ID parameter
            pst.setInt(7, Integer.parseInt(txtIdProduit.getText())); // Setting the product ID parameter
            int affectedRows = pst.executeUpdate(); // Executing the insertion query
            // Show appropriate message based on the result of the insertion
            if (affectedRows > 0) {
                System.out.println("Commande added successfully."); // Showing success message
                loadTableData(); // Reloading table data
            } else {
                System.out.println("No commande was added."); // Showing failure message
            }
        } catch (SQLException e) {
            System.err.println("Failed to add commande: " + e.getMessage()); // Handling SQL errors
        }
    }

    // Update commande button action
    @FXML
    void Update(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Commande selectedCommande = table.getSelectionModel().getSelectedItem(); // Getting the selected commande
            String sql = "UPDATE Commande SET Date_Commande=?, Date_Livraison_Prevue=?, Statut_Commande=?, Montant_Total=?, ID_Fournisseur=?, ID_Personnel=?, ID_Produit=? WHERE ID_Commande=?";
            try {
                pst = con.prepareStatement(sql); // Preparing the SQL statement
                pst.setDate(1, Date.valueOf(txtDateCommande.getValue())); // Setting the commande date parameter
                pst.setDate(2, Date.valueOf(txtDateLivraisonPrevue.getValue())); // Setting the expected delivery date parameter
                pst.setString(3, txtStatutCommande.getText()); // Setting the commande status parameter
                pst.setDouble(4, Double.parseDouble(txtMontantTotal.getText())); // Setting the total amount parameter
                pst.setInt(5, Integer.parseInt(txtIdFournisseur.getText())); // Setting the supplier ID parameter
                pst.setInt(6, Integer.parseInt(txtIdPersonnel.getText())); // Setting the personnel ID parameter
                pst.setInt(7, Integer.parseInt(txtIdProduit.getText())); // Setting the product ID parameter
                pst.setInt(8, selectedCommande.getIdCommande()); // Setting the commande ID parameter
                int affectedRows = pst.executeUpdate(); // Executing the update query
                // Show appropriate message based on the result of the update
                if (affectedRows > 0) {
                    System.out.println("Commande updated successfully."); // Showing success message
                    loadTableData(); // Reloading table data
                } else {
                    System.out.println("No commande was updated."); // Showing failure message
                }
            } catch (SQLException e) {
                System.err.println("Failed to update commande: " + e.getMessage()); // Handling SQL errors
            }
        } else {
            System.out.println("No commande selected for update."); // Showing an alert if no commande is selected
        }
    }

    // Delete commande button action
    @FXML
    void Delete(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Commande selectedCommande = table.getSelectionModel().getSelectedItem(); // Getting the selected commande
            String sql = "DELETE FROM Commande WHERE ID_Commande=?";
            try {
                pst = con.prepareStatement(sql); // Preparing the SQL statement
                pst.setInt(1, selectedCommande.getIdCommande()); // Setting the commande ID parameter
                int affectedRows = pst.executeUpdate(); // Executing the deletion query
                // Show appropriate message based on the result of the deletion
                if (affectedRows > 0) {
                    System.out.println("Commande deleted successfully."); // Showing success message
                    loadTableData(); // Reloading table data
                } else {
                    System.out.println("No commande was deleted."); // Showing failure message
                }
            } catch (SQLException e) {
                System.err.println("Failed to delete commande: " + e.getMessage()); // Handling SQL errors
            }
        } else {
            System.out.println("No commande selected for deletion."); // Showing an alert if no commande is selected
        }
    }
}
