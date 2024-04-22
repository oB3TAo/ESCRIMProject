package com.example.escrimproject;

import com.example.escrimproject.architecture.Fournisseur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;


public class FournisseurController {

    @FXML private TableView<Fournisseur> table;
    @FXML private TableColumn<Fournisseur, Integer> IDFournisseurColmn;
    @FXML private TableColumn<Fournisseur, String> NomColmn;
    @FXML private TableColumn<Fournisseur, String> AdresseColmn;
    @FXML private TableColumn<Fournisseur, String> TelephoneColmn;
    @FXML private TableColumn<Fournisseur, String> EmailColmn;
    @FXML private TableColumn<Fournisseur, String> ContactPrincipalColmn;

    @FXML private TextField txtNom;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtContactPrincipal;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    @FXML
    public void initialize() {
        if (areComponentsLoaded()) {
            connectDatabase();
            setupCellValueFactories();
            loadTableData();
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
            System.err.println("UI components are not initialized!");
        }
    }

    private boolean areComponentsLoaded() {
        // Assurez-vous que tous les composants FX:ID sont non nulls
        return table != null && IDFournisseurColmn != null && NomColmn != null
                && AdresseColmn != null && TelephoneColmn != null && EmailColmn != null
                && ContactPrincipalColmn != null;
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "GtAlMsS=32=460M");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

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
            e.printStackTrace();
        }
    }


    private void setupCellValueFactories() {
        IDFournisseurColmn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        NomColmn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        AdresseColmn.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
        TelephoneColmn.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
        EmailColmn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        ContactPrincipalColmn.setCellValueFactory(cellData -> cellData.getValue().contactPrincipalProperty());
    }

    private void loadTableData() {
        ObservableList<Fournisseur> fournisseurs = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Fournisseur");
            rs = pst.executeQuery();
            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur();
                fournisseur.setId(rs.getInt("ID_Fournisseur"));
                fournisseur.setNom(rs.getString("Nom"));
                fournisseur.setAdresse(rs.getString("Adresse"));
                fournisseur.setTelephone(rs.getString("Telephone"));
                fournisseur.setEmail(rs.getString("Email"));
                fournisseur.setContactPrincipal(rs.getString("Contact_Principal"));
                fournisseurs.add(fournisseur);
            }
            table.setItems(fournisseurs);
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    @FXML
    private void Add(ActionEvent event) {
        String sql = "INSERT INTO Fournisseur (Nom, Adresse, Telephone, Email, Contact_Principal) VALUES (?, ?, ?, ?, ?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, txtNom.getText());
            pst.setString(2, txtAdresse.getText());
            pst.setString(3, txtTelephone.getText());
            pst.setString(4, txtEmail.getText());
            pst.setString(5, txtContactPrincipal.getText());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Fournisseur added successfully.");
                loadTableData();
            } else {
                System.out.println("No fournisseur was added.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add fournisseur: " + e.getMessage());
        }
    }

    @FXML
    void Update(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Fournisseur selectedFournisseur = table.getSelectionModel().getSelectedItem();
            String sql = "UPDATE Fournisseur SET Nom=?, Adresse=?, Telephone=?, Email=?, Contact_Principal=? WHERE ID_Fournisseur=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setString(2, txtAdresse.getText());
                pst.setString(3, txtTelephone.getText());
                pst.setString(4, txtEmail.getText());
                pst.setString(5, txtContactPrincipal.getText());
                pst.setInt(6, selectedFournisseur.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Fournisseur updated successfully.");
                    loadTableData();
                } else {
                    System.out.println("No fournisseur was updated.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to update fournisseur: " + e.getMessage());
            }
        } else {
            System.out.println("No fournisseur selected for update.");
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Fournisseur selectedFournisseur = table.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM Fournisseur WHERE ID_Fournisseur=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, selectedFournisseur.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Fournisseur deleted successfully.");
                    loadTableData();
                } else {
                    System.out.println("No fournisseur was deleted.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to delete fournisseur: " + e.getMessage());
            }
        } else {
            System.out.println("No fournisseur selected for deletion.");
        }
    }
}
