package com.example.escrimproject;

import com.example.escrimproject.architecture.Categorie;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class CategoryController {

    @FXML private TableView<Categorie> tableCategories;
    @FXML private TableColumn<Categorie, Integer> colCategoryId;
    @FXML private TableColumn<Categorie, String> colCategoryName;
    @FXML private TableColumn<Categorie, String> colCategoryDescription;
    @FXML private TextField txtCategoryName;
    @FXML private TextField txtCategoryDescription;
    @FXML private Button btnAddCategory;
    @FXML private Button btnUpdateCategory;
    @FXML private Button btnDeleteCategory;

    private Connection con;

    @FXML
    public void initialize() {
        // Initialize database connection and table view
        connectToDatabase();
        initializeTableView();
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
        // Set up cell value factories and possibly load categories from database
    }

    // Implement CRUD operations for categories here
}

