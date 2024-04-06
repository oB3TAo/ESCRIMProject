package com.example.escrimproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainTabController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab colisTab;

    @FXML
    private Tab materielsTab;

    @FXML
    private Tab commandesTab;

    private Tab personnelTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // You can initialize anything related to the controller here
        // For example, setting up event listeners or loading initial data
    }
}
