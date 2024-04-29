package com.example.escrimproject;

import com.example.escrimproject.architecture.Patient;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for managing patient data in the application.
 */
public class PatientController implements Initializable {

    @FXML
    private TableView<Patient> table; // TableView to display patient data
    @FXML
    private TableColumn<Patient, Integer> IDColmn; // Column for patient ID
    @FXML
    private TableColumn<Patient, String> NomColmn; // Column for patient name
    @FXML
    private TableColumn<Patient, LocalDate> DateNaissanceColmn; // Column for patient date of birth
    @FXML
    private TableColumn<Patient, String> SexeColmn; // Column for patient gender
    @FXML
    private TableColumn<Patient, String> SSNColmn; // Column for patient SSN
    @FXML
    private TableColumn<Patient, String> AdresseColmn; // Column for patient address
    @FXML
    private TableColumn<Patient, String> TelephoneColmn; // Column for patient telephone number
    @FXML
    private TableColumn<Patient, String> EmailColmn; // Column for patient email
    @FXML
    private TableColumn<Patient, String> TraitementColmn; // Column for patient treatment
    @FXML
    private TableColumn<Patient, String> DiagnosticColmn; // Column for patient diagnosis
    @FXML
    private TableColumn<Patient, String> StatutColmn; // Column for patient status
    @FXML
    private TableColumn<Patient, Integer> IDPersonnelColmn; // Column for personnel ID associated with the patient

    // Text fields for entering patient data
    @FXML
    private TextField txtNom;
    @FXML
    private DatePicker txtDateNaissance;
    @FXML
    private ComboBox<String> cmbSexe;
    @FXML
    private TextField txtSSN;
    @FXML
    private TextField txtAdresse;
    @FXML
    private TextField txtTelephone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTraitement;
    @FXML
    private TextField txtDiagnostic;
    @FXML
    private ComboBox<String> cmbStatut;
    @FXML
    private TextField txtIDPersonnel;

    // Buttons for adding, updating, and deleting patients
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    // Database connection variables
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    /**
     * Initializes the controller class.
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectDatabase(); // Establish database connection
        if (table != null) {
            setupCellValueFactories(); // Setup cell value factories for table columns
            loadTableData(); // Load data into the table
            initializeComboBoxes(); // Initialize combo boxes for gender and status
            setupRowSelectListener(); // Setup listener for table row selection
        } else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }
    }

    /**
     * Initializes the combo boxes for gender and status.
     */
    private void initializeComboBoxes() {
        cmbSexe.setItems(FXCollections.observableArrayList("M", "F"));
        cmbStatut.setItems(FXCollections.observableArrayList("Active", "Inactive"));
    }

    /**
     * Sets up a listener for table row selection.
     */
    private void setupRowSelectListener() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormFields(newSelection);
            }
        });
    }

    /**
     * Fills the form fields with selected patient data.
     * @param patient The selected patient.
     */
    private void fillFormFields(Patient patient) {
        txtNom.setText(patient.getNom());
        txtDateNaissance.setValue(patient.getDateDeNaissance());
        cmbSexe.setValue(patient.getSexe());
        txtSSN.setText(patient.getNumeroSecuriteSocial());
        txtAdresse.setText(patient.getAdresse());
        txtTelephone.setText(patient.getNumeroTelephone());
        txtEmail.setText(patient.getEmail());
        txtTraitement.setText(patient.getTraitementEnCours());
        txtDiagnostic.setText(patient.getDiagnostic());
        cmbStatut.setValue(patient.getStatut());
        txtIDPersonnel.setText(Integer.toString(patient.getIdPersonnel()));
    }

    /**
     * Establishes a connection to the database.
     */
    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error Connecting: " + ex.getMessage());
        }
    }

    /**
     * Handles the logout action.
     * @param event The action event.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up cell value factories for table columns.
     */
    private void setupCellValueFactories() {
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
        IDPersonnelColmn.setCellValueFactory(cellData -> cellData.getValue().idPersonnelProperty().asObject());
    }

    /**
     * Loads data into the table from the database.
     */
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
                newPatient.setIdPersonnel(rs.getInt("ID_Personnel"));
                patients.add(newPatient);
            }
            table.setItems(patients);
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error Loading Data: " + ex.getMessage());
        }
    }

    /**
     * Shows an alert dialog with the given message.
     * @param message The message to be displayed.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles adding a new patient to the database.
     * @param event The action event.
     */
    @FXML
    private void Add(ActionEvent event) {
        String sql = "INSERT INTO Patient (Nom, Date_de_Naissance, Sexe, Numero_Securite_Social, Adresse, Numero_Telephone, Email, Traitement_en_Cours, Diagnostic, Statut, ID_Personnel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, txtNom.getText());
            pst.setDate(2, txtDateNaissance.getValue() != null ? Date.valueOf(txtDateNaissance.getValue()) : null);
            pst.setString(3, cmbSexe.getValue());
            pst.setString(4, txtSSN.getText());
            pst.setString(5, txtAdresse.getText());
            pst.setString(6, txtTelephone.getText());
            pst.setString(7, txtEmail.getText());
            pst.setString(8, txtTraitement.getText());
            pst.setString(9, txtDiagnostic.getText());
            pst.setString(10, cmbStatut.getValue());
            pst.setInt(11, Integer.parseInt(txtIDPersonnel.getText()));

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                showAlert("Patient added successfully.");
                loadTableData();  // Refresh table data
            } else {
                showAlert("No patient was added.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Failed to add patient: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            showAlert("ID Personnel must be an integer.");
        }
    }

    /**
     * Handles updating an existing patient in the database.
     * @param event The action event.
     */
    @FXML
    void Update(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Patient selectedPatient = table.getSelectionModel().getSelectedItem();
            String sql = "UPDATE Patient SET Nom=?, Date_de_Naissance=?, Sexe=?, Numero_Securite_Social=?, Adresse=?, Numero_Telephone=?, Email=?, Traitement_en_Cours=?, Diagnostic=?, Statut=?, ID_Personnel=? WHERE ID_Patient=?";
            try {
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setDate(2, Date.valueOf(txtDateNaissance.getValue()));
                pst.setString(3, cmbSexe.getValue());
                pst.setString(4, txtSSN.getText());
                pst.setString(5, txtAdresse.getText());
                pst.setString(6, txtTelephone.getText());
                pst.setString(7, txtEmail.getText());
                pst.setString(8, txtTraitement.getText());
                pst.setString(9, txtDiagnostic.getText());
                pst.setString(10, cmbStatut.getValue());
                pst.setInt(11, Integer.parseInt(txtIDPersonnel.getText()));
                pst.setInt(12, selectedPatient.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Patient updated successfully.");
                    loadTableData();  // Refresh table data
                } else {
                    showAlert("No patient was updated.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert("Failed to update patient: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                showAlert("ID Personnel must be an integer.");
            }
        } else {
            showAlert("No patient selected for update.");
        }
    }

    /**
     * Handles deleting an existing patient from the database.
     * @param event The action event.
     */
    @FXML
    void Delete(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Patient selectedPatient = table.getSelectionModel().getSelectedItem();
            String sql = "DELETE FROM Patient WHERE ID_Patient=?";
            try {
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, selectedPatient.getId());

                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Patient deleted successfully.");
                    loadTableData();  // Refresh table data
                } else {
                    showAlert("No patient was deleted.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert("Failed to delete patient: " + ex.getMessage());
            }
        } else {
            showAlert("No patient selected for deletion.");
        }
    }
}
