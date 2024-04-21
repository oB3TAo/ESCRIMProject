package com.example.escrimproject;

import com.example.escrimproject.architecture.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryController {

    @FXML
    private TableView<Categorie> tableCategories;
    @FXML
    private TableColumn<Categorie, Integer> colCategoryId;
    @FXML
    private TableColumn<Categorie, String> colCategoryName;
    @FXML
    private TableColumn<Categorie, String> colCategoryDescription;
    @FXML
    private TextField txtCategoryName;
    @FXML
    private TextField txtCategoryDescription;
    @FXML
    private Button btnAddCategory;
    @FXML
    private Button btnUpdateCategory;
    @FXML
    private Button btnDeleteCategory;

    private Connection con;

    @FXML
    public void initialize() {
        connectToDatabase();
        if (tableCategories != null) {
            initializeTableView();
        } else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }

    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeTableView() {
        colCategoryId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colCategoryName.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        colCategoryDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Load categories from the database
        loadCategories();
    }

    private void loadCategories() {
        try {
            ObservableList<Categorie> categories = FXCollections.observableArrayList();
            String sql = "SELECT * FROM Categorie";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Categorie category = new Categorie();
                category.setId(rs.getInt("ID_Categorie"));
                category.setNom(rs.getString("Nom_Categorie"));
                category.setDescription(rs.getString("Description_Categorie"));
                categories.add(category);
            }
            tableCategories.setItems(categories);
        } catch (SQLException ex) {
            showAlert("Error loading categories: " + ex.getMessage());
        }
    }

    @FXML
    private void Add() {
        String categoryName = txtCategoryName.getText().trim();
        String categoryDescription = txtCategoryDescription.getText().trim();

        if (categoryName.isEmpty()) {
            showAlert("Category name cannot be empty.");
            return;
        }

        try {
            String sql = "INSERT INTO Categorie (Nom_Categorie, Description_Categorie) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, categoryName);
            pstmt.setString(2, categoryDescription);
            pstmt.executeUpdate();

            showAlert("Category added successfully.");
            loadCategories();
            clearFields();
        } catch (SQLException ex) {
            showAlert("Error adding category: " + ex.getMessage());
        }
    }

    @FXML
    private void Update() {
        Categorie selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert("Please select a category to update.");
            return;
        }

        String newName = txtCategoryName.getText().trim();
        String newDescription = txtCategoryDescription.getText().trim();

        if (newName.isEmpty()) {
            showAlert("Category name cannot be empty.");
            return;
        }

        try {
            String sql = "UPDATE Categorie SET Nom_Categorie=?, Description_Categorie=? WHERE ID_Categorie=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setString(2, newDescription);
            pstmt.setInt(3, selectedCategory.getId());
            pstmt.executeUpdate();

            showAlert("Category updated successfully.");
            loadCategories();
            clearFields();
        } catch (SQLException ex) {
            showAlert("Error updating category: " + ex.getMessage());
        }
    }

    @FXML
    private void Delete() {
        Categorie selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert("Please select a category to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete this category?");
        confirmationAlert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        String sql = "DELETE FROM Categorie WHERE ID_Categorie=?";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setInt(1, selectedCategory.getId());
                        pstmt.executeUpdate();

                        showAlert("Category deleted successfully.");
                        loadCategories();
                        clearFields();
                    } catch (SQLException ex) {
                        showAlert("Error deleting category: " + ex.getMessage());
                    }
                });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtCategoryName.clear();
        txtCategoryDescription.clear();
        tableCategories.getSelectionModel().clearSelection();
    }
}
