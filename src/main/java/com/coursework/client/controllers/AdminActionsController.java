package com.coursework.client.controllers;

import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminActionsController {

    @FXML
    private void onBackToMenuClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/start.fxml");
    }

    @FXML
    public void onUserEditClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/crudUser.fxml");
    }

    public void onCompanyEditClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/crudCompany.fxml");
    }

    public void onCheckoutClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneNavigator.switchScene(stage, "/com/coursework/client/userActions.fxml");
    }

    @FXML
    private void onSettingsClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/serverSettings.fxml");
    }

    public void onStatsClicked(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/stats.fxml");
    }

    @FXML
    public void openLogs(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/logView.fxml");
    }

    @FXML
    private void logout(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/login.fxml");
    }
}
