package com.example.escrimproject;

import com.example.escrimproject.architecture.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonnelController implements Initializable {

    @FXML private TextField txtNom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    @FXML private TextField txtStatut;
    @FXML private TextField txtSpecialite;
    @FXML private TableView<Personnel> table;
    @FXML private TableColumn<Personnel, String> IDColmn;
    @FXML private TableColumn<Personnel, String> NomColmn;
    @FXML private TableColumn<Personnel, String> EmailColmn;
    @FXML private TableColumn<Personnel, String> TelephoneColmn;
    @FXML private TableColumn<Personnel, String> StatutColmn;
    @FXML private TableColumn<Personnel, String> SpecialiteColmn;
    @FXML private ComboBox<String> cmbType;
    @FXML private ComboBox<String> cmbStatut;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    private Connection con;
    private PreparedStatement pst;
    private int id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Connect();
        if (table != null) {
            initializeComboBox();
            table();
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


    private void initializeComboBox() {
        cmbType.setItems(FXCollections.observableArrayList("Medecin", "Pharmacien", "Logisticien"));
        cmbStatut.setItems(FXCollections.observableArrayList("Active", "Inactive"));
    }

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


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Personnel");
        alert.setHeaderText("Personnel");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtNom.clear();
        txtEmail.clear();
        txtTelephone.clear();
        cmbStatut.getSelectionModel().clearSelection();
        txtSpecialite.clear();
        cmbType.getSelectionModel().clearSelection();
    }

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
                personnels.add(p);
            }
            table.setItems(personnels);
            setCellValueFactories();
        } catch (SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }

    private void setCellValueFactories() {
        IDColmn.setCellValueFactory(f -> f.getValue().idProperty().asString());
        NomColmn.setCellValueFactory(f -> f.getValue().nomProperty());
        EmailColmn.setCellValueFactory(f -> f.getValue().emailProperty());
        TelephoneColmn.setCellValueFactory(f -> f.getValue().telephoneProperty());
        StatutColmn.setCellValueFactory(f -> f.getValue().statutProperty());
        SpecialiteColmn.setCellValueFactory(f -> {
            if (f.getValue() instanceof Medecin) {
                return ((Medecin) f.getValue()).specialiteProperty();
            } else {
                return null;
            }
        });
    }

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

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim", "root", "MySQL_B3TA90");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PersonnelController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert("Error: " + ex.getMessage());
        }
    }
}
