package com.example.escrimproject;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;


public class HelloController implements Initializable {


    @FXML
    private Label label;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtRole;

    @FXML
    private TextField txtItems;

    @FXML
    private TableView<Personnel> table;

    @FXML
    private TableColumn<Personnel, String> IDColmn;

    @FXML
    private TableColumn<Personnel, String> NameColmn;

    @FXML
    private TableColumn<Personnel, String> RoleColmn;

    @FXML
    private TableColumn<Personnel, String> ItemsColmn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    void Add(ActionEvent event) {

        String stname,role,items;
        stname = txtName.getText();
        role = txtRole.getText();
        items = txtItems.getText();
        try
        {
            pst = con.prepareStatement("insert into registration(name,role,items)values(?,?,?)");
            pst.setString(1, stname);
            pst.setString(2, role);
            pst.setString(3, items);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personnel Registration");


            alert.setHeaderText("Personnel Registration");
            alert.setContentText("Record Added!");

            alert.showAndWait();

            table();

            txtName.setText("");
            txtRole.setText("");
            txtItems.setText("");
            txtName.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void table()
    {
        Connect();
        ObservableList<Personnel> personnels = FXCollections.observableArrayList();
        try
        {
            pst = con.prepareStatement("select id,name,role,items from registration");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    Personnel st = new Personnel();
                    st.setId(rs.getString("id"));
                    st.setName(rs.getString("name"));
                    st.setRole(rs.getString("role"));
                    st.setItems(rs.getString("items"));
                    personnels.add(st);
                }
            }
            table.setItems(personnels);
            IDColmn.setCellValueFactory(f -> f.getValue().idProperty());
            NameColmn.setCellValueFactory(f -> f.getValue().nameProperty());
            RoleColmn.setCellValueFactory(f -> f.getValue().roleProperty());
            ItemsColmn.setCellValueFactory(f -> f.getValue().itemsProperty());



        }

        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }

        table.setRowFactory( tv -> {
            TableRow<Personnel> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    myIndex =  table.getSelectionModel().getSelectedIndex();

                    id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
                    txtName.setText(table.getItems().get(myIndex).getName());
                    txtRole.setText(table.getItems().get(myIndex).getRole());
                    txtItems.setText(table.getItems().get(myIndex).getItems());



                }
            });
            return myRow;
        });


    }

    @FXML
    void Delete(ActionEvent event) {
        myIndex = table.getSelectionModel().getSelectedIndex();

        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));


        try
        {
            pst = con.prepareStatement("delete from registration where id = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personnel Registration");


            alert.setHeaderText("Personnel Registration");
            alert.setContentText("Deleted!");

            alert.showAndWait();
            table();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Update(ActionEvent event) {

        String stname,role,items;

        myIndex = table.getSelectionModel().getSelectedIndex();

        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        stname = txtName.getText();
        role = txtRole.getText();
        items = txtItems.getText();
        try
        {
            pst = con.prepareStatement("update registration set name = ?,role = ? ,items = ? where id = ? ");
            pst.setString(1, stname);
            pst.setString(2, role);
            pst.setString(3, items);
            pst.setInt(4, id);
            pst.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Personnel Registration");


            alert.setHeaderText("Personnel Registration");
            alert.setContentText("Updated!");

            alert.showAndWait();
            table();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    Connection con;
    PreparedStatement pst;
    int myIndex;
    int id;


    public void Connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escrim","root","MySQL_B3TA90");
        } catch (ClassNotFoundException ex) {

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        table();
    }

}