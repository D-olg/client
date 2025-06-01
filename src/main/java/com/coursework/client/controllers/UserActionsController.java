package com.coursework.client.controllers;

import com.coursework.client.utils.SceneNavigator;
import com.coursework.client.session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class UserActionsController {

    @FXML
    private void onBackToAdmin(ActionEvent event) {
        if (Session.isAdmin()) {
            SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    "/com/coursework/client/adminActions.fxml");
        }
    }

    @FXML
    private void createScenario(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/createScenario.fxml");
    }

    @FXML
    private void openScenarios(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/scenarioList.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/login.fxml");
    }

    public void onCalculationClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/calculation.fxml");
    }

    public void onImportClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/import.fxml");
    }

    public void onExportClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/export.fxml");
    }

    public void onHistoryClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/history.fxml");
    }

    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/start.fxml");
    }

    public void onProfileClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/profile.fxml");
    }
}
