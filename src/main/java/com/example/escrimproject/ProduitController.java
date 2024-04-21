package com.example.escrimproject;

import com.example.escrimproject.architecture.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProduitController implements Initializable {

    @FXML private TableColumn<Produit, String> DDPColmn;
    @FXML private TableColumn<Produit, String> IDColmn;
    @FXML private TableColumn<Produit, String> IdCatColmn;
    @FXML private TableColumn<Produit, String> NomColmn;
    @FXML private TableColumn<Produit, String> PoidsColmn;
    @FXML private TableColumn<Produit, String> QuantiteColmn;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnUpdate;
    @FXML private ComboBox<String> cmbTypeProd;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private DatePicker dateDDP;
    @FXML private TableView<Produit> table;
    @FXML private TextField txtNom;
    @FXML private TextField txtPoids;
    @FXML private TextField txtQuantite;

    private Connection con;
    private PreparedStatement pst;

    @FXML
    void Add(ActionEvent event) {
        String nom = txtNom.getText();
        String poids = txtPoids.getText();
        String quantite = txtQuantite.getText();
        String dateDePeremption = (dateDDP.getValue() != null) ? dateDDP.getValue().toString() : null;
        String typeProd = cmbTypeProd.getValue();
        String categorySelection = cmbCategory.getValue();
        if (nom.isEmpty() || poids.isEmpty() || quantite.isEmpty() || categorySelection.equals("Add New Category...")) {
            showAlert("Please fill in all required fields and select a valid category.");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categorySelection.split(" - ")[0]);
            String sql = "INSERT INTO Produit(Nom, ID_Categorie, Poids, Quantite, Type_Prod, DateDePeremption) VALUES (?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setInt(2, categoryId);
            pst.setFloat(3, Float.parseFloat(poids));
            pst.setInt(4, Integer.parseInt(quantite));
            pst.setString(5, typeProd);
            if (typeProd.equals("Medicament")) {
                pst.setString(6, dateDePeremption);
            } else {
                pst.setNull(6, Types.DATE);
            }
            pst.executeUpdate();
            showAlert("Record Added!");
            refreshTable();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        Produit selectedProduit = table.getSelectionModel().getSelectedItem();
        if (selectedProduit == null) {
            showAlert("No product selected for deletion.");
            return;
        }
        try {
            pst = con.prepareStatement("DELETE FROM Produit WHERE ID_Produit = ?");
            pst.setInt(1, selectedProduit.getId());
            pst.executeUpdate();
            showAlert("Product deleted!");
            refreshTable();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    void Update(ActionEvent event) {
        Produit selectedProduit = table.getSelectionModel().getSelectedItem();
        if (selectedProduit == null) {
            showAlert("No product selected for update.");
            return;
        }
        String nom = txtNom.getText();
        String poids = txtPoids.getText();
        String quantite = txtQuantite.getText();
        String typeProd = cmbTypeProd.getValue();
        String dateDePeremption = (dateDDP.getValue() != null) ? dateDDP.getValue().toString() : null;

        try {
            String sql = "UPDATE Produit SET Nom = ?, Poids = ?, Quantite = ?, Type_Prod = ?, DateDePeremption = ? WHERE ID_Produit = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setFloat(2, Float.parseFloat(poids));
            pst.setInt(3, Integer.parseInt(quantite));
            pst.setString(4, typeProd);
            pst.setString(5, dateDePeremption);
            pst.setInt(6, selectedProduit.getId());
            pst.executeUpdate();
            showAlert("Product updated successfully!");
            refreshTable();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void initializeComboBoxes() {
        cmbTypeProd.setItems(FXCollections.observableArrayList("Medicament", "Materiel"));
        cmbCategory.setItems(FXCollections.observableArrayList("Add New Category..."));
        loadCategories();
    }

    private void loadCategories() {
        try {
            String sql = "SELECT ID_Categorie, Nom_Categorie FROM Categorie";
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cmbCategory.getItems().add(rs.getInt("ID_Categorie") + " - " + rs.getString("Nom_Categorie"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error loading categories: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        // Populate this list based on your database schema and queries
        // Example has been omitted for brevity
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        initializeComboBoxes(); // Call initializeComboBoxes() first
        Platform.runLater(() -> {
            refreshTable();
        });
    }


}
