module com.coursework.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires okhttp3;
    requires java.sql;

    opens com.coursework.client to javafx.fxml;
    opens com.coursework.client.controllers to javafx.fxml;
    opens com.coursework.client.models to javafx.fxml, com.fasterxml.jackson.databind, javafx.base;
    opens com.coursework.client.session to javafx.fxml, org.controlsfx.controls;

    // Экспортируем DTO-пакет только для Jackson
    exports com.coursework.client.dto to com.fasterxml.jackson.databind;
    exports com.coursework.client;
    exports com.coursework.client.controllers;
    exports com.coursework.client.models;
    opens com.coursework.client.dto to com.fasterxml.jackson.databind, javafx.fxml, javafx.base;
}
