package com.example.escrimproject;

import com.example.escrimproject.architecture.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for managing personnel data in the application.
 */
public class PersonnelController implements Initializable {

    @FXML private TextField txtNom; // Text field for personnel name
    @FXML private TextField txtEmail; // Text field for personnel email
    @FXML private TextField txtTelephone; // Text field for personnel telephone
    @FXML private TextField txtStatut; // Text field for personnel status
    @FXML private TextField txtSpecialite; // Text field for personnel specialty
    @FXML private TableView<Personnel> table; // TableView to display personnel data
    @FXML private TableColumn<Personnel, String> IDColmn; // Column for personnel ID
    @FXML private TableColumn<Personnel, String> NomColmn; // Column for personnel name
    @FXML private TableColumn<Personnel, String> EmailColmn; // Column for personnel email
    @FXML private TableColumn<Personnel, String> TelephoneColmn; // Column for personnel telephone
    @FXML private TableColumn<Personnel, String> StatutColmn; // Column for personnel status
    @FXML private TableColumn<Personnel, String> SpecialiteColmn; // Column for personnel specialty
    @FXML private TableColumn<Personnel, String> TypeColmn; // Column for personnel type
    @FXML private ComboBox<String> cmbType; // ComboBox for personnel type
    @FXML private ComboBox<String> cmbStatut; // ComboBox for personnel status
    @FXML private Button btnAdd; // Button to add personnel
    @FXML private Button btnUpdate; // Button to update personnel
    @FXML private Button btnDelete; // Button to delete personnel

    private Connection con; // Database connection variable
    private PreparedStatement pst; // Prepared statement for database queries
    private int id; // Personnel ID

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect(); // Establish database connection
        if (table != null) {
            initializeComboBox(); // Initialize combo boxes for type and status
            table(); // Load data into the table
            // Add listener to cmbType ComboBox
            cmbType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if ("Medecin".equals(newVal)) {
                    txtSpecialite.setDisable(false); // Enable Spécialité TextField
                    txtSpecialite.setPromptText("Required");
                } else {
                    txtSpecialite.setDisable(true); // Disable Spécialité TextField
                    txtSpecialite.setPromptText(""); // Remove prompt text
                }
            });

            // Add listener to table TableView
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    // Update TextField and ComboBox values with selected row's data
                    txtNom.setText(newVal.getNom());
                    txtEmail.setText(newVal.getEmail());
                    txtTelephone.setText(newVal.getTelephone());
                    cmbStatut.setValue(newVal.getStatut());

                    if (newVal instanceof Medecin) {
                        txtSpecialite.setText(((Medecin) newVal).getSpecialite());
                        cmbType.setValue("Medecin");
                    } else if (newVal instanceof Pharmacien) {
                        txtSpecialite.setText(""); // Clear Spécialité TextField
                        cmbType.setValue("Pharmacien");
                    } else if (newVal instanceof Logisticien) {
                        txtSpecialite.setText(""); // Clear Spécialité TextField
                        cmbType.setValue("Logisticien");
                    }
                }
            });
        }else {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, "TableView is not initialized");
        }
    }

    /**
     * Handles the logout action.
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
     * Initializes the combo boxes for personnel type and status.
     */
    private void initializeComboBox() {
        cmbType.setItems(FXCollections.observableArrayList("Medecin", "Pharmacien", "Logisticien"));
        cmbStatut.setItems(FXCollections.observableArrayList("Active", "Inactive"));
    }

    /**
     * Handles adding a new personnel to the database.
     * @param event The action event.
     */
    @FXML
    void Add(ActionEvent event) {
        String nom = txtNom.getText();
        String email = txtEmail.getText();
        String telephone = txtTelephone.getText();
        String statut = cmbStatut.getValue();
        String specialite = txtSpecialite.getText();
        String type = cmbType.getValue();

        if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || statut.isEmpty() || type.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        if ("Medecin".equals(type) && specialite.isEmpty()) {
            showAlert("Please fill in the Spécialité field for Médecin.");
            return;
        }

        try {
            String sql;
            if ("Medecin".equals(type)) {
                sql = "INSERT INTO Personnel(Nom, Email, Telephone, Statut, Specialite, Type) VALUES (?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT INTO Personnel(Nom, Email, Telephone, Statut, Type) VALUES (?, ?, ?, ?, ?)";
            }

            pst = con.prepareStatement(sql);
            pst.setString(1, nom);
            pst.setString(2, email);
            pst.setString(3, telephone);
            pst.setString(4, statut);
            if ("Medecin".equals(type)) {
                pst.setString(5, specialite);
                pst.setString(6, type);
            } else {
                pst.setString(5, type);
            }
            pst.executeUpdate();

            showAlert("Record Added!");
            table();

            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        txtNom.clear();
        txtEmail.clear();
        txtTelephone.clear();
        cmbStatut.getSelectionModel().clearSelection();
        txtSpecialite.clear();
        cmbType.getSelectionModel().clearSelection();
    }

    /**
     * Loads data into the table from the database.
     */
    public void table() {
        ObservableList<Personnel> personnels = FXCollections.observableArrayList();
        try {
            pst = con.prepareStatement("SELECT ID_Personnel, Nom, Email, Telephone, Statut, Specialite, Type FROM Personnel");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                Personnel p;
                switch (type) {
                    case "Pharmacien":
                        p = new Pharmacien();
                        break;
                    case "Medecin":
                        p = new Medecin();
                        ((Medecin) p).setSpecialite(rs.getString("Specialite"));
                        break;
                    case "Logisticien":
                        p = new Logisticien();
                        break;
                    default:
                        continue; // Skip unknown type
                }
                p.setId(rs.getInt("ID_Personnel"));
                p.setNom(rs.getString("Nom"));
                p.setEmail(rs.getString("Email"));
                p.setTelephone(rs.getString("Telephone"));
                p.setStatut(rs.getString("Statut"));
                p.setType(rs.getString("Type"));
                personnels.add(p);
            }
            table.setItems(personnels);
            setCellValueFactories();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Sets cell value factories for the table columns.
     */
    private void setCellValueFactories() {
        IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
        NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
        EmailColmn.setCellValueFactory(f -> f.getValue().emailProperty());
        TelephoneColmn.setCellValueFactory(f -> f.getValue().telephoneProperty());
        StatutColmn.setCellValueFactory(f -> f.getValue().statutProperty());
        TypeColmn.setCellValueFactory(f -> f.getValue().typeProperty());
        SpecialiteColmn.setCellValueFactory(f -> {
            if (f.getValue() instanceof Medecin) {
                return ((Medecin) f.getValue()).specialiteProperty();
            } else {
                return null;
            }
        });
    }

    /**
     * Handles deleting an existing personnel from the database.
     * @param event The action event.
     */
    @FXML
    void Delete(ActionEvent event) {
        Personnel selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Please select a row to delete.");
            return;
        }
        int id = selectedItem.getId();

        try {
            pst = con.prepareStatement("DELETE FROM Personnel WHERE ID_Personnel = ?");
            pst.setInt(1, id);
            pst.executeUpdate();

            showAlert("Deleted!");
            table();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Handles updating an existing personnel in the database.
     * @param event The action event.
     */
    @FXML
    void Update(ActionEvent event) {
        Personnel selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Please select a row to update.");
            return;
        }
        int id = selectedItem.getId();
        String nom = txtNom.getText();
        String email = txtEmail.getText();
        String telephone = txtTelephone.getText();
        String statut = cmbStatut.getValue();

        try {
            pst = con.prepareStatement("UPDATE Personnel SET Nom = ?, Email = ?, Telephone = ?, Statut = ? WHERE ID_Personnel = ?");
            pst.setString(1, nom);
            pst.setString(2, email);
            pst.setString(3, telephone);
            pst.setString(4, statut);
            pst.setInt(5, id);
            pst.executeUpdate();

            showAlert("Updated!");
            table();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Establishes a connection to the database.
     */
    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    /**
     * Displays an alert dialog with the given message.
     * @param message The message to display.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Personnel");
        alert.setHeaderText("Personnel");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
