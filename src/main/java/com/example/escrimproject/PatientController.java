package com.example.escrimproject;

import com.example.escrimproject.architecture.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientController implements Initializable {

    @FXML private TableView<Patient> table;
    @FXML private TableColumn<Patient, Integer> IDColmn;
    @FXML private TableColumn<Patient, String> NomColmn;
    @FXML private TableColumn<Patient, LocalDate> DateNaissanceColmn;
    @FXML private TableColumn<Patient, String> SexeColmn;
    @FXML private TableColumn<Patient, String> SSNColmn;
    @FXML private TableColumn<Patient, String> AdresseColmn;
    @FXML private TableColumn<Patient, String> TelephoneColmn;
    @FXML private TableColumn<Patient, String> EmailColmn;
    @FXML private TableColumn<Patient, String> TraitementColmn;
    @FXML private TableColumn<Patient, String> DiagnosticColmn;
    @FXML private TableColumn<Patient, String> StatutColmn;

    @FXML private TextField txtNom;
    @FXML private DatePicker txtDateNaissance;
    @FXML private ComboBox<String> cmbSexe;
    @FXML private TextField txtSSN;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTraitement;
    @FXML private TextField txtDiagnostic;
    @FXML private ComboBox<String> cmbStatut;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectDatabase();
        if (table != null) {
            setupCellValueFactories();
            loadTableData();
            initializeComboBoxes();
        } else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }
    }

    private void initializeComboBoxes() {
        cmbSexe.setItems(FXCollections.observableArrayList("M", "F"));
        cmbStatut.setItems(FXCollections.observableArrayList("Active", "Inactive"));
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "GtAlMsS=32=460M");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error Connecting: " + ex.getMessage());
        }
    }

    private void setupCellValueFactories() {
        if (table != null) {
            IDColmn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
            NomColmn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
            DateNaissanceColmn.setCellValueFactory(cellData -> cellData.getValue().dateDeNaissanceProperty());
            SexeColmn.setCellValueFactory(cellData -> cellData.getValue().sexeProperty());
            SSNColmn.setCellValueFactory(cellData -> cellData.getValue().numeroSecuriteSocialProperty());
            AdresseColmn.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
            TelephoneColmn.setCellValueFactory(cellData -> cellData.getValue().numeroTelephoneProperty());
            EmailColmn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
            TraitementColmn.setCellValueFactory(cellData -> cellData.getValue().traitementEnCoursProperty());
            DiagnosticColmn.setCellValueFactory(cellData -> cellData.getValue().diagnosticProperty());
            StatutColmn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        } else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "Table is not initialized");
        }
    }

    private void loadTableData() {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT * FROM Patient");
            rs = pst.executeQuery();
            while (rs.next()) {
                Patient newPatient = new Patient();
                newPatient.setId(rs.getInt("ID_Patient"));
                newPatient.setNom(rs.getString("Nom"));
                newPatient.setDateDeNaissance(rs.getDate("Date_de_Naissance").toLocalDate());
                newPatient.setSexe(rs.getString("Sexe"));
                newPatient.setNumeroSecuriteSocial(rs.getString("Numero_Securite_Social"));
                newPatient.setAdresse(rs.getString("Adresse"));
                newPatient.setNumeroTelephone(rs.getString("Numero_Telephone"));
                newPatient.setEmail(rs.getString("Email"));
                newPatient.setDiagnostic(rs.getString("Diagnostic"));
                newPatient.setTraitementEnCours(rs.getString("Traitement_en_Cours"));
                newPatient.setStatut(rs.getString("Statut"));
                patients.add(newPatient);
            }
            table.setItems(patients);
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error Loading Data: " + ex.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddButtonClick(ActionEvent event) {
        // Implementation for adding a patient
    }

    @FXML
    void addPatient(ActionEvent event) {
        // Implementation for adding a patient
    }

    @FXML
    void updatePatient(ActionEvent event) {
        // Implementation for updating a patient
    }

    @FXML
    void deletePatient(ActionEvent event) {
        // Implementation for deleting a patient
    }
}
