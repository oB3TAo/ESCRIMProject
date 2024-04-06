package com.example.escrimproject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableRow;

public class MaterielsController implements Initializable {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtCategorie;

    @FXML
    private TextField txtQuantite;

    @FXML
    private TextField txtDateAchat;

    @FXML
    private TextField txtDerniereVerification;

    @FXML
    private TextField txtDatePeremption;

    @FXML
    private TextField txtFournisseur;

    @FXML
    private TextField txtCout;

    @FXML
    private TableView<Materiel> table;

    @FXML
    private TableColumn<Materiel, String> IDColmn;

    @FXML
    private TableColumn<Materiel, String> NomColmn;

    @FXML
    private TableColumn<Materiel, String> CategorieColmn;

    @FXML
    private TableColumn<Materiel, String> QuantiteColmn;

    @FXML
    private TableColumn<Materiel, String> DateAchatColmn;

    @FXML
    private TableColumn<Materiel, String> DerniereVerificationColmn;

    @FXML
    private TableColumn<Materiel, String> DatePeremptionColmn;

    @FXML
    private TableColumn<Materiel, String> FournisseurColmn;

    @FXML
    private TableColumn<Materiel, String> CoutColmn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    private Connection con;
    private PreparedStatement pst;
    private int myIndex;
    private int id;

    @FXML
    void Add(ActionEvent event) {
        String nom = txtNom.getText();
        String categorie = txtCategorie.getText();
        String quantite = txtQuantite.getText();
        String dateAchat = txtDateAchat.getText();
        String derniereVerification = txtDerniereVerification.getText();
        String datePeremption = txtDatePeremption.getText();
        String fournisseur = txtFournisseur.getText();
        String cout = txtCout.getText();

        try {
            int categoryId = getCategoryID(categorie);
            if (categoryId == -1) {
                // If the category doesn't exist, create it
                categoryId = createCategory(categorie);
            }

            int supplierId = getSupplierID(fournisseur);
            if (supplierId == -1) {
                // If the supplier doesn't exist, create it
                supplierId = createSupplier(fournisseur);
            }

            pst = con.prepareStatement("INSERT INTO Materiel(Nom, Categorie, Quantite_Disponible, Date_Achat, Derniere_Verification, Date_Peremption, Fournisseur, Cout) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, nom);
            pst.setInt(2, categoryId);
            pst.setString(3, quantite);
            pst.setString(4, dateAchat);
            pst.setString(5, derniereVerification);
            pst.setString(6, datePeremption);
            pst.setInt(7, supplierId);
            pst.setString(8, cout);
            pst.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Materiel", "Record Added!", "Record added successfully!");

            loadTableData();
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int createCategory(String categoryName) {
        try {
            pst = con.prepareStatement("INSERT INTO Categorie(Nom_Categorie) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, categoryName);
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated category ID
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private int createSupplier(String supplierName) {
        try {
            pst = con.prepareStatement("INSERT INTO Fournisseur(Nom) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, supplierName);
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated supplier ID
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }


    private int getCategoryID(String categoryName) {
        try {
            pst = con.prepareStatement("SELECT ID_Categorie FROM Categorie WHERE Nom_Categorie = ?");
            pst.setString(1, categoryName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_Categorie");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private int getSupplierID(String supplierName) {
        try {
            pst = con.prepareStatement("SELECT ID_Fournisseur FROM Fournisseur WHERE Nom = ?");
            pst.setString(1, supplierName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_Fournisseur");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }


    @FXML
    void Update(ActionEvent event) {
        Materiel selectedMateriel = table.getSelectionModel().getSelectedItem();
        if (selectedMateriel == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No Material Selected", "Please select a material to update.");
            return;
        }

        String nom = txtNom.getText();
        String categorie = txtCategorie.getText();
        String quantite = txtQuantite.getText();
        String dateAchat = txtDateAchat.getText();
        String derniereVerification = txtDerniereVerification.getText();
        String datePeremption = txtDatePeremption.getText();
        String fournisseur = txtFournisseur.getText();
        String cout = txtCout.getText();

        try {
            int categoryId = getCategoryID(categorie);
            if (categoryId == -1) {
                // If the category doesn't exist, create it
                categoryId = createCategory(categorie);
            }

            int supplierId = getSupplierID(fournisseur);
            if (supplierId == -1) {
                // If the supplier doesn't exist, create it
                supplierId = createSupplier(fournisseur);
            }

            pst = con.prepareStatement("UPDATE Materiel SET Nom=?, Categorie=?, Quantite_Disponible=?, Date_Achat=?, Derniere_Verification=?, Date_Peremption=?, Fournisseur=?, Cout=? WHERE ID_Materiel=?");
            pst.setString(1, nom);
            pst.setInt(2, categoryId);
            pst.setString(3, quantite);
            pst.setString(4, dateAchat);
            pst.setString(5, derniereVerification);
            pst.setString(6, datePeremption);
            pst.setInt(7, supplierId);
            pst.setString(8, cout);
            pst.setInt(9, Integer.parseInt(selectedMateriel.getId()));
            pst.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Materiel", "Record Updated!", "Record updated successfully!");

            loadTableData();
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        Materiel selectedMateriel = table.getSelectionModel().getSelectedItem();
        if (selectedMateriel == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No Material Selected", "Please select a material to delete.");
            return;
        }

        try {
            pst = con.prepareStatement("DELETE FROM Materiel WHERE ID_Materiel=?");
            pst.setInt(1, Integer.parseInt(selectedMateriel.getId()));
            pst.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Materiel", "Record Deleted!", "Record deleted successfully!");

            loadTableData();
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        loadTableData();

        // Handle table selection change
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update text fields with the values of the selected item
                txtNom.setText(newSelection.getNom());
                txtCategorie.setText(newSelection.getCategorie());
                txtQuantite.setText(newSelection.getQuantite());
                txtDateAchat.setText(newSelection.getDateAchat());
                txtDerniereVerification.setText(newSelection.getDerniereVerification());
                txtDatePeremption.setText(newSelection.getDatePeremption());
                txtFournisseur.setText(newSelection.getFournisseur());
                txtCout.setText(newSelection.getCout());
            }
        });
    }


    private void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTableData() {
        ObservableList<Materiel> materiels = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Materiel");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Materiel mat = new Materiel();
                mat.setId(rs.getString("ID_Materiel"));
                mat.setNom(rs.getString("Nom"));
                mat.setCategorie(rs.getString("Categorie"));
                mat.setQuantite(rs.getString("Quantite_Disponible"));
                mat.setDateAchat(rs.getString("Date_Achat"));
                mat.setDerniereVerification(rs.getString("Derniere_Verification"));
                mat.setDatePeremption(rs.getString("Date_Peremption"));
                mat.setFournisseur(rs.getString("Fournisseur"));
                mat.setCout(rs.getString("Cout"));
                materiels.add(mat);
            }
            table.setItems(materiels);
            IDColmn.setCellValueFactory(f -> f.getValue().idProperty());
            NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
            CategorieColmn.setCellValueFactory(f -> f.getValue().categorieProperty());
            QuantiteColmn.setCellValueFactory(f -> f.getValue().quantiteProperty());
            DateAchatColmn.setCellValueFactory(f -> f.getValue().dateAchatProperty());
            DerniereVerificationColmn.setCellValueFactory(f -> f.getValue().derniereVerificationProperty());
            DatePeremptionColmn.setCellValueFactory(f -> f.getValue().datePeremptionProperty());
            FournisseurColmn.setCellValueFactory(f -> f.getValue().fournisseurProperty());
            CoutColmn.setCellValueFactory(f -> f.getValue().coutProperty());
        } catch (SQLException ex) {
            Logger.getLogger(MaterielsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtCategorie.clear();
        txtQuantite.clear();
        txtDateAchat.clear();
        txtDerniereVerification.clear();
        txtDatePeremption.clear();
        txtFournisseur.clear();
        txtCout.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
