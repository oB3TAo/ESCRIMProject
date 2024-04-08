package com.example.escrimproject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.escrimproject.architecture.Personnel;
import com.example.escrimproject.architecture.Pharmacien;
import com.example.escrimproject.architecture.Medecin;
import com.example.escrimproject.architecture.Logisticien;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class HelloController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelephone;

    @FXML
    private TextField txtStatut;

    @FXML
    private TextField txtSpecialite; // Assuming this is used for the speciality field for Medecin

    @FXML
    private TableView<Personnel> table;

    @FXML
    private TableColumn<Personnel, String> IDColmn;

    @FXML
    private TableColumn<Personnel, String> NomColmn;

    @FXML
    private TableColumn<Personnel, String> EmailColmn;

    @FXML
    private TableColumn<Personnel, String> TelephoneColmn;

    @FXML
    private TableColumn<Personnel, String> StatutColmn;

    @FXML
    private TableColumn<Personnel, String> SpecialiteColmn; // Added for Medecin

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    void Add(ActionEvent event) {
        String nom, email, telephone, statut, specialite;
        nom = txtNom.getText();
        email = txtEmail.getText();
        telephone = txtTelephone.getText();
        statut = txtStatut.getText();
        specialite = txtSpecialite.getText(); // Assuming this is used for the speciality field for Medecin
        try {
            pst = con.prepareStatement("INSERT INTO Personnel(Nom, Email, Telephone, Statut, Specialite) VALUES (?, ?, ?, ?, ?)");
            pst.setString(1, nom);
            pst.setString(2, email);
            pst.setString(3, telephone);
            pst.setString(4, statut);
            pst.setString(5, specialite); // Assuming this is used for the speciality field for Medecin
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personnel personnel");
            alert.setHeaderText("Personnel personnel");
            alert.setContentText("Record Added!");
            alert.showAndWait();

            table();

            txtNom.setText("");
            txtEmail.setText("");
            txtTelephone.setText("");
            txtStatut.setText("");
            txtSpecialite.setText(""); // Assuming this is used for the speciality field for Medecin
            txtNom.requestFocus();
        } catch (SQLException ex) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void table() {
        Connect();
        ObservableList<Personnel> personnels = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT ID_Personnel, Nom, Email, Telephone, Statut, Specialite FROM Personnel");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String specialite = rs.getString("Specialite");
                Personnel p;
                if ("Pharmacien".equals(specialite)) {
                    p = new Pharmacien();
                } else if ("Medecin".equals(specialite)) {
                    p = new Medecin();
                } else if ("Logisticien".equals(specialite)) {
                    p = new Logisticien();
                } else {
                    // Handle unknown speciality
                    continue;
                }
                p.setId(rs.getInt("ID_Personnel"));
                p.setNom(rs.getString("Nom"));
                p.setEmail(rs.getString("Email"));
                p.setTelephone(rs.getString("Telephone"));
                p.setStatut(rs.getString("Statut"));
                if (p instanceof Medecin) {
                    ((Medecin) p).setSpecialite(specialite);
                }
                personnels.add(p);
            }
            table.setItems(personnels);
            IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
            NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
            EmailColmn.setCellValueFactory(f -> f.getValue().emailProperty());
            TelephoneColmn.setCellValueFactory(f -> f.getValue().telephoneProperty());
            StatutColmn.setCellValueFactory(f -> f.getValue().statutProperty());
            SpecialiteColmn.setCellValueFactory(f -> {
                if (f.getValue() instanceof Medecin) {
                    return ((Medecin) f.getValue()).specialiteProperty();
                } else {
                    return null;
                }
            });
        } catch (SQLException ex) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        myIndex = table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        try {
            pst = con.prepareStatement("delete from personnel where id = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personnel personnel");
            alert.setHeaderText("Personnel personnel");
            alert.setContentText("Deleted!");
            alert.showAndWait();
            table();
        } catch (SQLException ex) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Update(ActionEvent event) {
        String nom, email, telephone, statut;

        myIndex = table.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        nom = txtNom.getText();
        email = txtEmail.getText();
        telephone = txtTelephone.getText();
        statut = txtStatut.getText();

        try {
            pst = con.prepareStatement("update Personnel set Nom = ?, Email = ?, Telephone = ?, Statut = ? where ID_Personnel = ?");
            pst.setString(1, nom);
            pst.setString(2, email);
            pst.setString(3, telephone);
            pst.setString(4, statut);
            pst.setInt(5, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personnel personnel");
            alert.setHeaderText("Personnel personnel");
            alert.setContentText("Updated!");
            alert.showAndWait();

            table();
        } catch (SQLException ex) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Connection con;
    PreparedStatement pst;
    int myIndex;
    int id;

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        table();
    }

}
