package com.example.escrimproject;

import com.example.escrimproject.architecture.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryController {

    @FXML
    private TableView<Category> tableCategories;
    @FXML
    private TableColumn<Category, Integer> colCategoryId;
    @FXML
    private TableColumn<Category, String> colCategoryName;
    @FXML
    private TableColumn<Category, String> colCategoryDescription;

    @FXML
    private TextField txtCategoryName;
    @FXML
    private TextField txtCategoryDescription;

    @FXML
    private Button btnAddCategory;
    @FXML
    private Button btnDeleteCategory;
    @FXML
    private Button btnUpdateCategory;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    @FXML
    public void initialize() {
        connectDatabase();
        if (tableCategories != null) {

            setupCellValueFactories();
            loadTableData();
            tableCategories.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Category category = tableCategories.getSelectionModel().getSelectedItem();
                    txtCategoryName.setText(category.getName());
                    txtCategoryDescription.setText(category.getDescription());
                }
            });
        } else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void setupCellValueFactories() {
        colCategoryId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colCategoryName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colCategoryDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }

    private void loadTableData() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Category");
            rs = pst.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("ID_Category"));
                category.setName(rs.getString("Name"));
                category.setDescription(rs.getString("Description"));
                categories.add(category);
            }
            tableCategories.setItems(categories);
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    @FXML
    private void Add(ActionEvent event) {
        String categoryName = txtCategoryName.getText();
        String categoryDescription = txtCategoryDescription.getText();

        if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        try {
            String sql = "INSERT INTO Category (Name, Description) VALUES (?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, categoryName);
            pst.setString(2, categoryDescription);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Category Added!");
                loadTableData();
                clearFields();
            } else {
                showAlert("Failed to add category.");
            }
        } catch (SQLException ex) {
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    private void Update(ActionEvent event) {
        if (tableCategories.getSelectionModel().getSelectedItem() != null) {
            Category selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
            String categoryName = txtCategoryName.getText();
            String categoryDescription = txtCategoryDescription.getText();

            if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
                showAlert("Please fill in all required fields.");
                return;
            }

            String sql = "UPDATE Category SET Name=?, Description=? WHERE ID_Category=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, categoryName);
                pst.setString(2, categoryDescription);
                pst.setInt(3, selectedCategory.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Category updated successfully.");
                    loadTableData();
                } else {
                    showAlert("No category was updated.");
                }
            } catch (SQLException e) {
                showAlert("Failed to update category: " + e.getMessage());
            }
        } else {
            showAlert("No category selected for update.");
        }
    }

    @FXML
    private void Delete(ActionEvent event) {
        if (tableCategories.getSelectionModel().getSelectedItem() != null) {
            Category selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM Category WHERE ID_Category=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, selectedCategory.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Category deleted successfully.");
                    loadTableData();
                } else {
                    showAlert("No category was deleted.");
                }
            } catch (SQLException e) {
                showAlert("Failed to delete category: " + e.getMessage());
            }
        } else {
            showAlert("No category selected for deletion.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category");
        alert.setHeaderText("Category");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtCategoryName.clear();
        txtCategoryDescription.clear();
    }
}
