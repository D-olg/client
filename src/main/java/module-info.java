module com.coursework.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.coursework.client to javafx.fxml;
    opens com.coursework.client.controllers to javafx.fxml;
    opens com.coursework.client.models to javafx.fxml, org.controlsfx.controls;

    exports com.coursework.client;
}
