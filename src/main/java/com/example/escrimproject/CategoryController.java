package com.example.escrimproject;

import com.example.escrimproject.architecture.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class CategoryController {

    @FXML private TableView<Category> tableCategories;
    @FXML private TableColumn<Category, Integer> colCategoryId;
    @FXML private TableColumn<Category, String> colCategoryName;
    @FXML private TableColumn<Category, String> colCategoryDescription;

    @FXML private TextField txtCategoryName;
    @FXML private TextField txtCategoryDescription;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    @FXML
    public void initialize() {
        if (areComponentsLoaded()) {
            connectDatabase();
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
            System.err.println("UI components are not initialized!");
        }
    }

    private boolean areComponentsLoaded() {
        // Make sure all FX:ID components are not null
        return tableCategories != null && colCategoryId != null && colCategoryName != null
                && colCategoryDescription != null;
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
        String sql = "INSERT INTO Category (Name, Description) VALUES (?, ?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, txtCategoryName.getText());
            pst.setString(2, txtCategoryDescription.getText());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Category added successfully.");
                loadTableData();
            } else {
                System.out.println("No category was added.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add category: " + e.getMessage());
        }
    }

    @FXML
    void Update(ActionEvent event) {
        if (tableCategories.getSelectionModel().getSelectedItem() != null) {
            Category selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
            String sql = "UPDATE Category SET Name=?, Description=? WHERE ID_Category=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtCategoryName.getText());
                pst.setString(2, txtCategoryDescription.getText());
                pst.setInt(3, selectedCategory.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Category updated successfully.");
                    loadTableData();
                } else {
                    System.out.println("No category was updated.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to update category: " + e.getMessage());
            }
        } else {
            System.out.println("No category selected for update.");
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        if (tableCategories.getSelectionModel().getSelectedItem() != null) {
            Category selectedCategory = tableCategories.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM Category WHERE ID_Category=?";
            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, selectedCategory.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Category deleted successfully.");
                    loadTableData();
                } else {
                    System.out.println("No category was deleted.");
                }
            } catch (SQLException e) {
                System.err.println("Failed to delete category: " + e.getMessage());
            }
        } else {
            System.out.println("No category selected for deletion.");
        }
    }
}
