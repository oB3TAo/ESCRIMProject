package com.example.escrimproject;

import com.example.escrimproject.architecture.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
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
    @FXML private TableColumn<Produit, String> TypeColmn;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnUpdate;
    @FXML private ComboBox<String> cmbType;
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
        String type = cmbType.getValue();
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
            pst.setString(5, type);
            if (type.equals("Medicament")) {
                pst.setString(6, dateDePeremption);
            } else {
                pst.setNull(6, Types.DATE);
            }
            pst.executeUpdate();
            showAlert("Record Added!");
            table();

            clearFields();
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
            table();
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
        String type = cmbType.getValue();
        String dateDePeremption = (dateDDP.getValue() != null) ? dateDDP.getValue().toString() : null;

        try {
            String sql = "UPDATE Produit SET Nom = ?, Poids = ?, Quantite = ?, Type_Prod = ?, DateDePeremption = ? WHERE ID_Produit = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setFloat(2, Float.parseFloat(poids));
            pst.setInt(3, Integer.parseInt(quantite));
            pst.setString(4, type);
            pst.setString(5, dateDePeremption);
            pst.setInt(6, selectedProduit.getId());
            pst.executeUpdate();
            showAlert("Product updated successfully!");
            table();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void initializeComboBox() {
        cmbType.setItems(FXCollections.observableArrayList("Medicament", "Materiel"));
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

    private void setCellValueFactories() {
        IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
        NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
        PoidsColmn.setCellValueFactory(f -> f.getValue().poidsProperty());
        QuantiteColmn.setCellValueFactory(f -> f.getValue().quantiteProperty());
        TypeColmn.setCellValueFactory(f -> f.getValue().typeProperty());
        IdCatColmn.setCellValueFactory(f -> f.getValue().categoryIdProperty());
        DDPColmn.setCellValueFactory(f -> {
            if (f.getValue() instanceof Medicament) {
                return (ObservableValue<String>) ((Medicament) f.getValue()).dateDePeremptionProperty();
            } else {
                return null;
            }
        });

        // If you want to display the category name instead of its ID:
        // IdCatColmn.setCellValueFactory(f -> {
        //     int categoryId = f.getValue().getCategoryId();
        //     // Use a method to retrieve the category name from the database based on the categoryId
        //     String categoryName = getCategoryName(categoryId);
        //     return new SimpleStringProperty(categoryName);
        // });
    }

    public void table() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT p.ID_Produit, p.Nom, p.Poids, p.Quantite, p.DateDePeremption, p.Type_Prod, c.ID_Categorie FROM Produit p JOIN Categorie c ON p.ID_Categorie = c.ID_Categorie");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type_Prod");
                Produit p;
                switch (type) {
                    case "Materiel":
                        p = new Materiel();
                        break;
                    case "Medicament":
                        p = new Medicament();
                        ((Medicament) p).setDateDePeremption(rs.getDate("DateDePeremption"));
                        break;
                    default:
                        continue; // Skip unknown type
                }
                p.setId(rs.getInt("ID_Produit"));
                p.setNom(rs.getString("Nom"));
                p.setPoids(rs.getFloat("Poids"));
                p.setQuantite(rs.getInt("Quantite"));
                p.setType(rs.getString("Type_Prod"));
                p.setCategoryId(String.valueOf(rs.getInt("ID_Categorie")));
                produits.add(p);
            }
            table.setItems(produits);
            setCellValueFactories();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtPoids.clear();
        txtQuantite.clear();
        cmbType.getSelectionModel().clearSelection();
        cmbCategory.getSelectionModel().clearSelection();
        dateDDP.getEditor().clear(); // Clear DatePicker editor
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeComboBox();
        Connect();
        loadCategories(); // Load categories into the ComboBox
        table();

        // Add listener to cmbType ComboBox
        cmbType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Medicament".equals(newVal)) {
                dateDDP.setDisable(false); // Enable dateDDP TextField
                dateDDP.setPromptText("Required");
            } else {
                dateDDP.setDisable(true); // Disable dateDDP TextField
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
                cmbType.setValue(newVal.getType());

                if (newVal instanceof Medicament) {
                    dateDDP.setValue(LocalDate.parse(((Medicament) newVal).getDateDePeremption()));
                    cmbType.setValue("Medicament");
                } else if (newVal instanceof Materiel) {
                    dateDDP.setValue(null); // Clear DatePicker
                    cmbType.setValue("Materiel");
                }
            }
        });
        cmbCategory.setOnAction(event -> {
            String selectedCategory = cmbCategory.getValue();
            if (selectedCategory != null && selectedCategory.equals("Add New Category...")) {
                // Switch to the Category tab
                switchToCategoryTab();
            }
        });
    }
    private void switchToCategoryTab() {
        try {
            // Load the Category tab FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("category_tab.fxml"));
            Parent categoryTabContent = loader.load();

            // Create a new Stage for the Category tab
            Stage categoryStage = new Stage();
            categoryStage.setTitle("Category Tab");
            categoryStage.initModality(Modality.APPLICATION_MODAL);
            Scene categoryScene = new Scene(categoryTabContent);
            categoryStage.setScene(categoryScene);

            // Show the new Stage
            categoryStage.showAndWait(); // Use showAndWait() to wait for the new window to close before continuing

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading Category tab: " + e.getMessage());
        }
    }
}
