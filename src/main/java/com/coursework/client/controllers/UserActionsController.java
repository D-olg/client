package com.coursework.client.controllers;

import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class UserActionsController {
    public void onCalculationClicked(ActionEvent event) {
    }

    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/start.fxml");
    }
}
