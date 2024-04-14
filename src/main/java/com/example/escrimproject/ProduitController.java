package com.example.escrimproject;

import com.example.escrimproject.architecture.*;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ProduitController implements Initializable {

    @FXML
    private TableColumn<Produit, String> DDPColmn;

    @FXML
    private TableColumn<Produit, String> IDColmn;

    @FXML
    private TableColumn<Produit, String> IdCatColmn;

    @FXML
    private TableColumn<Produit, String> NomColmn;

    @FXML
    private TableColumn<Produit, String> PoidsColmn;

    @FXML
    private TableColumn<Produit, String> QuantiteColmn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbTypeProd;

    @FXML
    private DatePicker dateDDP;

    @FXML
    private TableView<Produit> table;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPoids;

    @FXML
    private TextField txtQuantite;


    @FXML
    void Add(ActionEvent event) {
        String nom, poids, quantite, dateDePeremption, typeProd;
        nom = txtNom.getText();
        poids = txtPoids.getText();
        quantite = txtQuantite.getText();
        dateDePeremption = String.valueOf(dateDDP.getValue());
        typeProd = (String) cmbTypeProd.getValue();
        if (nom.isEmpty() || poids.isEmpty() || quantite.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        if ("Medicament".equals(typeProd) && dateDePeremption.isEmpty()) {
            showAlert("Please fill in the DDP field for Medicament.");
            return;
        }
        //pst = con.prepareStatement();
        try {
            String sql;
            if ("Medicament".equals(typeProd)) {
                sql = "INSERT INTO Produit(Nom, Poids, Quantite, Type_Prod, DateDePeremption) VALUES (?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO Produit(Nom, Poids, Quantite, Type_Prod) VALUES (?, ?, ?, ?)";
            }

            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setString(2, poids);
            pst.setString(3, quantite);
            pst.setString(4, typeProd);
            if ("Medicament".equals(typeProd)) {
                pst.setString(5, dateDePeremption);
            }
            pst.executeUpdate();

            showAlert("Record Added!");
            table();

            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void initializeComboBox() {
        cmbTypeProd.setItems(FXCollections.observableArrayList("Medicament", "Materiel"));
    }

    private void clearFields() {
        txtNom.clear();
        txtPoids.clear();
        txtQuantite.clear();
        cmbTypeProd.getSelectionModel().clearSelection();
        dateDDP.setValue(null);
    }

    public void table() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT ID_Produit ,Nom, Poids, Quantite, Type_Prod, DateDePeremption FROM Produit");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String typeProd = rs.getString("Type_Prod");
                Produit p;
                switch (typeProd) {
                    case "Materiel":
                        p = new Materiel();
                        break;
                    case "Medicament":
                        p = new Medicament();
                        ((Medicament) p).setDateDePeremption(rs.getDate("DDP"));
                        break;
                    default:
                        continue; // Skip unknown type
                }
                p.setId(rs.getInt("ID_Personnel"));
                p.setNom(rs.getString("Nom"));
                p.setPoids(Float.parseFloat(rs.getString("Poids")));
                p.setQuantite(Float.parseFloat(rs.getString("Telephone")));
                produits.add(p);
            }
            table.setItems(produits);
            setCellValueFactories();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void setCellValueFactories() {
        IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
        NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
        PoidsColmn.setCellValueFactory(f -> f.getValue().poidsProperty());
        QuantiteColmn.setCellValueFactory(f -> f.getValue().quantiteProperty());
        DDPColmn.setCellValueFactory(f -> {
            if (f.getValue() instanceof Medicament) {
                return (ObservableValue<String>) ((Medicament) f.getValue()).dateDePeremptionProperty();
            } else {
                return null;
            }
        });
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
        initializeComboBox();
        Connect();
        table();

        // Add listener to cmbType ComboBox
        cmbTypeProd.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Medicament".equals(newVal)) {
                dateDDP.setDisable(false); // Enable DDP Date
                dateDDP.setPromptText("Required");
            } else {
                dateDDP.setDisable(true); // Disable DDP Date
                dateDDP.setPromptText(""); // Remove prompt text
            }
        });

        // Add listener to table TableView
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Update TextField and ComboBox values with selected row's data
                txtNom.setText(newVal.getNom());
                txtPoids.setText(String.valueOf(newVal.getPoids()));
                txtQuantite.setText(String.valueOf(newVal.getQuantite()));

                if (newVal instanceof Medicament) {
                    dateDDP.setValue(LocalDate.parse(((Medicament) newVal).getDateDePeremption()));
                    cmbTypeProd.setValue("Medicament");
                } else if (newVal instanceof Materiel) {
                    dateDDP.setValue(null); // Clear Spécialité TextField
                    cmbTypeProd.setValue("Materiel");
                }
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Personnel");
        alert.setHeaderText("Personnel");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
