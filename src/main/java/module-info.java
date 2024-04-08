module com.example.escrimproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.escrimproject to javafx.fxml;
    exports com.example.escrimproject;
    exports com.example.escrimproject.architecture;
    opens com.example.escrimproject.architecture to javafx.fxml;
}