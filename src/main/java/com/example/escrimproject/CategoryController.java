package com.example.escrimproject;

import com.example.escrimproject.architecture.Category; // Importing the Category class
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryController {

    // FXML elements
    @FXML
    private TableView<Category> tableCategories; // TableView to display categories
    @FXML
    private TableColumn<Category, Integer> colCategoryId; // TableColumn for category IDs
    @FXML
    private TableColumn<Category, String> colCategoryName; // TableColumn for category names
    @FXML
    private TableColumn<Category, String> colCategoryDescription; // TableColumn for category descriptions
    @FXML
    private TextField txtCategoryName; // TextField for entering category name
    @FXML
    private TextArea txtCategoryDescription; // TextArea for entering category description
    @FXML
    private Button btnAddCategory; // Button to add a category
    @FXML
    private Button btnDeleteCategory; // Button to delete a category
    @FXML
    private Button btnUpdateCategory; // Button to update a category

    // Database connection variables
    private Connection con; // Connection object
    private PreparedStatement pst; // PreparedStatement object
    private ResultSet rs; // ResultSet object

    // Initialize method called after loading the FXML file
    @FXML
    public void initialize() {
        connectDatabase(); // Establish database connection
        if (tableCategories != null) {
            setupCellValueFactories(); // Setup cell value factories for table columns
            loadTableData(); // Load data into the table
            // Listener to populate text fields when a row in the table is selected
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

    // Method to establish a connection to the database
    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Loading the MySQL JDBC driver
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90"); // Establishing connection to the database
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage()); // Handling connection errors
        }
    }

    // Setup cell value factories for table columns
    private void setupCellValueFactories() {
        colCategoryId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject()); // Binding category IDs to the corresponding TableColumn
        colCategoryName.setCellValueFactory(cellData -> cellData.getValue().nameProperty()); // Binding category names to the corresponding TableColumn
        colCategoryDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty()); // Binding category descriptions to the corresponding TableColumn
    }

    // Load data into the table
    private void loadTableData() {
        ObservableList<Category> categories = FXCollections.observableArrayList(); // ObservableList to hold Category objects
        try {
            pst = con.prepareStatement("SELECT * FROM Category"); // Preparing SQL statement to select all categories
            rs = pst.executeQuery(); // Executing the query
            while (rs.next()) {
                Category category = new Category(); // Creating a new Category object
                category.setId(rs.getInt("ID_Category")); // Setting the category ID
                category.setName(rs.getString("Name")); // Setting the category name
                category.setDescription(rs.getString("Description")); // Setting the category description
                categories.add(category); // Adding the Category object to the ObservableList
            }
            tableCategories.setItems(categories); // Setting the ObservableList as the items of the TableView
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage()); // Handling data loading errors
        }
    }

    // Add category button action
    @FXML
    private void Add(ActionEvent event) {
        String categoryName = txtCategoryName.getText(); // Getting the category name from the text field
        String categoryDescription = txtCategoryDescription.getText(); // Getting the category description from the text area

        // Validate input fields
        if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
            showAlert("Please fill in all required fields."); // Showing an alert if any field is empty
            return;
        }

        try {
            String sql = "INSERT INTO Category (Name, Description) VALUES (?, ?)"; // SQL statement to insert a new category
            pst = con.prepareStatement(sql); // Preparing the SQL statement
            pst.setString(1, categoryName); // Setting the category name parameter
            pst.setString(2, categoryDescription); // Setting the category description parameter
            int rowsAffected = pst.executeUpdate(); // Executing the insertion query

            // Show appropriate alert based on the result of the insertion
            if (rowsAffected > 0) {
                showAlert("Category Added!"); // Showing success message
                loadTableData(); // Reloading table data
                clearFields(); // Clearing input fields
            } else {
                showAlert("Failed to add category."); // Showing failure message
            }
        } catch (SQLException ex) {
            showAlert("Error: " + ex.getMessage()); // Handling SQL errors
        }
    }

    // Update category button action
    @FXML
    private void Update(ActionEvent event) {
        if (tableCategories.getSelectionModel().getSelectedItem() != null) {
            Category selectedCategory = tableCategories.getSelectionModel().getSelectedItem(); // Getting the selected category
            String categoryName = txtCategoryName.getText(); // Getting the updated category name
            String categoryDescription = txtCategoryDescription.getText(); // Getting the updated category description

            // Validate input fields
            if (categoryName.isEmpty() || categoryDescription.isEmpty()) {
                showAlert("Please fill in all required fields."); // Showing an alert if any field is empty
                return;
            }

            String sql = "UPDATE Category SET Name=?, Description=? WHERE ID_Category=?"; // SQL statement to update a category
            try {
                pst = con.prepareStatement(sql); // Preparing the SQL statement
                pst.setString(1, categoryName); // Setting the updated category name parameter
                pst.setString(2, categoryDescription); // Setting the updated category description parameter
                pst.setInt(3, selectedCategory.getId()); // Setting the category ID parameter

                int affectedRows = pst.executeUpdate(); // Executing the update query
                // Show appropriate alert based on the result of the update
                if (affectedRows > 0) {
                    showAlert("Category updated successfully."); // Showing success message
                    loadTableData(); // Reloading table data
                } else {
                    showAlert("No category was updated."); // Showing failure message
                }
            } catch (SQLException e) {
                showAlert("Failed to update category: " + e.getMessage()); // Handling SQL errors
            }
        } else {
            showAlert("No category selected for update."); // Showing an alert if no category is selected
        }
    }

    // Delete category button action
    @FXML
    private void Delete(ActionEvent event) {
        if (tableCategories.getSelectionModel().getSelectedItem() != null) {
            Category selectedCategory = tableCategories.getSelectionModel().getSelectedItem(); // Getting the selected category
            String sql = "DELETE FROM Category WHERE ID_Category=?"; // SQL statement to delete a category
            try {
                pst = con.prepareStatement(sql); // Preparing the SQL statement
                pst.setInt(1, selectedCategory.getId()); // Setting the category ID parameter

                int affectedRows = pst.executeUpdate(); // Executing the deletion query
                // Show appropriate alert based on the result of the deletion
                if (affectedRows > 0) {
                    showAlert("Category deleted successfully."); // Showing success message
                    loadTableData(); // Reloading table data
                } else {
                    showAlert("No category was deleted."); // Showing failure message
                }
            } catch (SQLException e) {
                showAlert("Failed to delete category: " + e.getMessage()); // Handling SQL errors
            }
        } else {
            showAlert("No category selected for deletion."); // Showing an alert if no category is selected
        }
    }

    // Method to show an alert dialog with the given message
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category");
        alert.setHeaderText("Category");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to clear text fields
    private void clearFields() {
        txtCategoryName.clear();
        txtCategoryDescription.clear();
    }
}
