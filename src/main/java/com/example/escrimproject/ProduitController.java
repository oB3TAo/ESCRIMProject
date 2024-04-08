package com.example.escrimproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import com.example.escrimproject.architecture.Produit;
import com.example.escrimproject.architecture.Medicament;
import com.example.escrimproject.architecture.Materiel;

public class ProduitController implements Initializable {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPoids;

    @FXML
    private TextField txtQuantite;

    @FXML
    private TableView<Produit> table;

    @FXML
    private TableColumn<Produit, String> IDColmn;

    @FXML
    private TableColumn<Produit, String> nomColmn;

    @FXML
    private TableColumn<Produit, String> PoidsColmn;

    @FXML
    private TableColumn<Produit, String> quantiteColmn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    void Add(ActionEvent event) {
        String nom, poids, quantite;
        nom = txtNom.getText();
        poids = txtPoids.getText();
        quantite = txtQuantite.getText();
        try {
            pst = con.prepareStatement("INSERT INTO Produit(Nom, Poids, Quantite) VALUES (?, ?, ?)");
            pst.setString(1, nom);
            pst.setString(2, poids);
            pst.setString(3, quantite);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produit");
            alert.setHeaderText("Produit");
            alert.setContentText("Record Added!");
            alert.showAndWait();

            table();

            txtNom.setText("");
            txtPoids.setText("");
            txtQuantite.setText("");
            txtNom.requestFocus();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void table() {
        Connect();
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT ID_Produit, Nom, Poids, Quantite FROM Produit");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Produit p;
                float poids = rs.getFloat("Poids");
                if (poids == 0) {
                    p = new Medicament();
                } else {
                    p = new Materiel();
                }
                p.setId(rs.getInt("ID_Produit"));
                p.setNom(rs.getString("Nom"));
                p.setPoids(poids);
                p.setQuantite(rs.getFloat("Quantite"));
                produits.add(p);
            }
            table.setItems(produits);
            IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
            nomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
            PoidsColmn.setCellValueFactory(f -> f.getValue().poidsProperty().asString());
            quantiteColmn.setCellValueFactory(f -> f.getValue().quantiteProperty().asString());
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        int myIndex = table.getSelectionModel().getSelectedIndex();
        int id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        try {
            pst = con.prepareStatement("delete from Produit where ID_Produit = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produit");
            alert.setHeaderText("Produit");
            alert.setContentText("Deleted!");
            alert.showAndWait();
            table();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Update(ActionEvent event) {
        String nom, poids, quantite;

        int myIndex = table.getSelectionModel().getSelectedIndex();
        int id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        nom = txtNom.getText();
        poids = txtPoids.getText();
        quantite = txtQuantite.getText();

        try {
            pst = con.prepareStatement("update Produit set Nom = ?, Poids = ?, Quantite = ? where ID_Produit = ?");
            pst.setString(1, nom);
            pst.setString(2, poids);
            pst.setString(3, quantite);
            pst.setInt(4, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Produit");
            alert.setHeaderText("Produit");
            alert.setContentText("Updated!");
            alert.showAndWait();

            table();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Connection con;
    PreparedStatement pst;

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
        table();
    }
}
