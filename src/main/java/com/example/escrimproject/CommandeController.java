package com.example.escrimproject;

import com.example.escrimproject.architecture.Commande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;

public class CommandeController {

    @FXML private TableView<Commande> table;
    @FXML private TableColumn<Commande, Integer> idCommandeCol;
    @FXML private TableColumn<Commande, LocalDate> dateCommandeCol;
    @FXML private TableColumn<Commande, LocalDate> dateLivraisonPrevueCol;
    @FXML private TableColumn<Commande, String> statutCommandeCol;
    @FXML private TableColumn<Commande, Double> montantTotalCol;
    @FXML private TableColumn<Commande, Integer> idFournisseurCol;
    @FXML private TableColumn<Commande, Integer> idPersonnelCol;
    @FXML private TableColumn<Commande, Integer> idProduitCol;

    @FXML private DatePicker txtDateCommande;
    @FXML private DatePicker txtDateLivraisonPrevue;
    @FXML private TextField txtStatutCommande;
    @FXML private TextField txtMontantTotal;
    @FXML private TextField txtIdFournisseur;
    @FXML private TextField txtIdPersonnel;
    @FXML private TextField txtIdProduit;

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
            System.err.println("UI components are not initialized!");
        }
    }

    private boolean areComponentsLoaded() {
        return table != null && idCommandeCol != null && dateCommandeCol != null
                && dateLivraisonPrevueCol != null && statutCommandeCol != null
                && montantTotalCol != null && idFournisseurCol != null
                && idPersonnelCol != null && idProduitCol != null;
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "GtAlMsS=32=460M");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

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

    private void loadTableData() {
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Commande");
            rs = pst.executeQuery();
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("ID_Commande"));
                commande.setDateCommande(rs.getDate("Date_Commande").toLocalDate());
                commande.setDateLivraisonPrevue(rs.getDate("Date_Livraison_Prevue").toLocalDate());
                commande.setStatutCommande(rs.getString("Statut_Commande"));
                commande.setMontantTotal(rs.getDouble("Montant_Total"));
                commande.setIdFournisseur(rs.getInt("ID_Fournisseur"));
                commande.setIdPersonnel(rs.getInt("ID_Personnel"));
                commande.setIdProduit(rs.getInt("ID_Produit"));
                commandes.add(commande);
            }
            table.setItems(commandes);
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    @FXML
    private void Add(ActionEvent event) {
        String sql = "INSERT INTO Commande (Date_Commande, Date_Livraison_Prevue, Statut_Commande, Montant_Total, ID_Fournisseur, ID_Personnel, ID_Produit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(txtDateCommande.getValue()));
            pst.setDate(2, Date.valueOf(txtDateLivraisonPrevue.getValue()));
            pst.setString(3, txtStatutCommande.getText());
            pst.setDouble(4, Double.parseDouble(txtMontantTotal.getText()));
            pst.setInt(5, Integer.parseInt(txtIdFournisseur.getText()));
            pst.setInt(6, Integer.parseInt(txtIdPersonnel.getText()));
            pst.setInt(7, Integer.parseInt(txtIdProduit.getText()));
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Commande added successfully.");
                loadTableData();
            } else {
                System.out.println("No commande was added.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add commande: " + e.getMessage());
        }
    }

    @FXML
    void Update(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Commande selectedCommande = table.getSelectionModel().getSelectedItem();
            String sql = "UPDATE Commande SET Date_Commande=?, Date_Livraison_Prevue=?, Statut_Commande=?, Montant_Total=?, ID_Fournisseur=?, ID_Personnel=?, ID_Produit=? WHERE ID_Commande=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setDate(1, Date.valueOf(txtDateCommande.getValue()));
                pst.setDate(2, Date.valueOf(txtDateLivraisonPrevue.getValue()));
                pst.setString(3, txtStatutCommande.getText());
                pst.setDouble(4, Double.parseDouble(txtMontantTotal.getText()));
                pst.setInt(5, Integer.parseInt(txtIdFournisseur.getText()));
                pst.setInt(6, Integer.parseInt(txtIdPersonnel.getText()));
                pst.setInt(7, Integer.parseInt(txtIdProduit.getText()));
                pst.setInt(8, selectedCommande.getIdCommande());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Commande updated successfully.");
                    loadTableData();
                } else {
                    System.out.println("No commande was updated.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to update commande: " + e.getMessage());
            }
        } else {
            System.out.println("No commande selected for update.");
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Commande selectedCommande = table.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM Commande WHERE ID_Commande=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, selectedCommande.getIdCommande());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Commande deleted successfully.");
                    loadTableData();
                } else {
                    System.out.println("No commande was deleted.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to delete commande: " + e.getMessage());
            }
        } else {
            System.out.println("No commande selected for deletion.");
        }
    }
}
