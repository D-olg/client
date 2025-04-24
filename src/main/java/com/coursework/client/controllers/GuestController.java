package com.coursework.client.controllers;

import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GuestController {

    @FXML
    private Label guestLabel;

    @FXML
    public void initialize() {
        guestLabel.setText("Вы вошли как гость. Некоторые функции могут быть недоступны.");

    }

    @FXML
    private void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/start.fxml");
    }
}
