package com.example.escrimproject;

import com.example.escrimproject.architecture.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

/**
 * Controller class for managing product data in the application.
 */
public class ProduitController implements Initializable {

    @FXML private TableColumn<Produit, String> DDPColmn; // Column for product DDP
    @FXML private TableColumn<Produit, String> IDColmn; // Column for product ID
    @FXML private TableColumn<Produit, String> IdCatColmn; // Column for product category ID
    @FXML private TableColumn<Produit, String> NomColmn; // Column for product name
    @FXML private TableColumn<Produit, String> PoidsColmn; // Column for product weight
    @FXML private TableColumn<Produit, String> QuantiteColmn; // Column for product quantity
    @FXML private TableColumn<Produit, String> TypeColmn; // Column for product type
    @FXML private Button btnAdd; // Button to add product
    @FXML private Button btnDelete; // Button to delete product
    @FXML private Button btnUpdate; // Button to update product
    @FXML private ComboBox<String> cmbType; // ComboBox for product type
    @FXML private ComboBox<String> cmbCategory; // ComboBox for product category
    @FXML private DatePicker dateDDP; // DatePicker for product DDP
    @FXML private TableView<Produit> table; // TableView to display product data
    @FXML private TextField txtNom; // TextField for product name
    @FXML private TextField txtPoids; // TextField for product weight
    @FXML private TextField txtQuantite; // TextField for product quantity

    private Connection con; // Database connection variable
    private PreparedStatement pst; // Prepared statement for database queries

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect(); // Establish database connection
        if (table != null) {
            initializeComboBox(); // Initialize combo boxes for type and category
            table(); // Load data into the table
            loadCategories(); // Load categories into the ComboBox

            // Add listener to cmbType ComboBox
            cmbType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if ("Medicament".equals(newVal)) {
                    dateDDP.setDisable(false); // Enable dateDDP DatePicker
                    dateDDP.setPromptText("Required");
                } else {
                    dateDDP.setDisable(true); // Disable dateDDP DatePicker
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

            // Add listener to cmbCategory ComboBox
            cmbCategory.setOnAction(event -> {
                String selectedCategory = cmbCategory.getValue();
                if (selectedCategory != null && selectedCategory.equals("Add New Category...")) {
                    // Switch to the Category tab
                    switchToCategoryTab();
                }
            });
        } else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }
    }

    /**
     * Handles adding a new product to the database.
     * @param event The action event.
     */
    @FXML
    void Add(ActionEvent event) {
        String nom = txtNom.getText();
        String poids = txtPoids.getText();
        String quantite = txtQuantite.getText();
        String dateDePeremption = (dateDDP.getValue() != null) ? dateDDP.getValue().toString() : null;
        String type = cmbType.getValue();
        String categorySelection = null;
        if (cmbCategory.getValue() != null) {
            String[] parts = cmbCategory.getValue().split(" - ");
            if (parts.length > 0) {
                categorySelection = parts[0]; // Keep only the ID part
            }
        }
        if (nom.isEmpty() || poids.isEmpty() || quantite.isEmpty() || categorySelection.equals("Add New Category...")) {
            showAlert("Please fill in all required fields and select a valid category.");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categorySelection.split(" - ")[0]);
            String sql = "INSERT INTO Produit(Nom, ID_Category, Poids, Quantite, Type_Prod, DateDePeremption) VALUES (?, ?, ?, ?, ?, ?)";
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

    /**
     * Handles deleting an existing product from the database.
     * @param event The action event.
     */
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

    /**
     * Handles updating an existing product in the database.
     * @param event The action event.
     */
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
        String categorySelection = null;
        if (cmbCategory.getValue() != null) {
            String[] parts = cmbCategory.getValue().split(" - ");
            if (parts.length > 0) {
                categorySelection = parts[0]; // Keep only the ID part
            }
        }

        try {
            String sql = "UPDATE Produit SET Nom = ?, Poids = ?, Quantite = ?, Type_Prod = ?, DateDePeremption = ?, ID_Category = ? WHERE ID_Produit = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setFloat(2, Float.parseFloat(poids));
            pst.setInt(3, Integer.parseInt(quantite));
            pst.setString(4, type);
            pst.setString(5, dateDePeremption);
            pst.setInt(6, Integer.parseInt(categorySelection)); // Set the category ID
            pst.setInt(7, selectedProduit.getId());
            pst.executeUpdate();

            showAlert("Product updated successfully!");
            table();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Initializes the combo boxes for product type and category.
     */
    private void initializeComboBox() {
        cmbType.setItems(FXCollections.observableArrayList("Medicament", "Materiel"));
        loadCategories();
    }

    /**
     * Loads categories into the category ComboBox.
     */
    public void loadCategories() {
        try {
            if (con == null) {
                showAlert("Database connection is null.");
                return;
            } else {
                // Clear all existing items before loading new categories
                cmbCategory.getItems().clear();

                // Add the "Add New Category..." option
                cmbCategory.getItems().add("Add New Category...");

                String sql = "SELECT ID_Category, Name FROM category";
                pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    cmbCategory.getItems().add(rs.getInt("ID_Category") + " - " + rs.getString("Name"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error loading category: " + ex.getMessage());
        }
    }

    /**
     * Displays an alert dialog with the given message.
     * @param message The message to display.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Establishes a connection to the database.
     */
    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the logout action by returning to the login view.
     * @param event The action event.
     */
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

    /**
     * Sets cell value factories for the table columns.
     */
    private void setCellValueFactories() {
        IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
        NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
        PoidsColmn.setCellValueFactory(f -> f.getValue().poidsProperty());
        QuantiteColmn.setCellValueFactory(f -> f.getValue().quantiteProperty());
        TypeColmn.setCellValueFactory(f -> f.getValue().typeProperty());
        //IdCatColmn.setCellValueFactory(f -> f.getValue().categoryIdProperty());
        DDPColmn.setCellValueFactory(f -> {
            if (f.getValue() instanceof Medicament) {
                Date dateDePeremption = Date.valueOf(((Medicament) f.getValue()).getDateDePeremption());
                if (dateDePeremption != null) {
                    return new SimpleStringProperty(dateDePeremption.toString());
                }
            }
            return null;
        });

        // If you want to display the category name instead of its ID:
        // IdCatColmn.setCellValueFactory(f -> {
        //     int categoryId = f.getValue().getCategoryId();
        //     // Use a method to retrieve the category name from the database based on the categoryId
        //     String categoryName = getCategoryName(categoryId);
        //     return new SimpleStringProperty(categoryName);
        // });
    }

    /**
     * Loads data into the table from the database.
     */
    public void table() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT p.ID_Produit, p.Nom, p.Poids, p.Quantite, p.DateDePeremption, p.Type_Prod, c.ID_Category FROM Produit p JOIN Category c ON p.ID_Category = c.ID_Category");
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
                p.setCategoryId(String.valueOf(rs.getInt("ID_Category")));
                produits.add(p);
            }
            table.setItems(produits);
            setCellValueFactories();
        } catch (SQLException ex) {
            Logger.getLogger(ProduitController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Clears the input fields.
     */
    private void clearFields() {
        txtNom.clear();
        txtPoids.clear();
        txtQuantite.clear();
        cmbType.getSelectionModel().clearSelection();
        cmbCategory.getSelectionModel().clearSelection();
        dateDDP.getEditor().clear(); // Clear DatePicker editor
    }

    /**
     * Switches to the Category tab.
     */
    private void switchToCategoryTab() {
        try {
            // Load the Category tab FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tab/category_tab.fxml"));
            Parent categoryTabContent = loader.load();

            // Create a new Stage for the Category tab
            Stage categoryStage = new Stage();
            categoryStage.setTitle("Category Tab");
            categoryStage.initModality(Modality.APPLICATION_MODAL);
            Scene categoryScene = new Scene(categoryTabContent, 495, 318);
            categoryStage.setScene(categoryScene);

            // Show the new Stage
            categoryStage.showAndWait(); // Use showAndWait() to wait for the new window to close before continuing

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading Category tab: " + e.getMessage());
        }
    }
}
