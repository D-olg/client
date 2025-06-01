package com.coursework.client.utils;

import com.coursework.client.session.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.io.IOException;

public class SceneNavigator {
    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            // Получаем размеры экрана
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            
            // Создаем сцену с размерами экрана
            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            
            // Загружаем стили в зависимости от роли
            String cssPath = Session.isAdmin() ? "/com/coursework/css/adminStyles.css" : "/com/coursework/css/styles.css";
            java.net.URL cssUrl = SceneNavigator.class.getResource(cssPath);
            System.out.println("CSS path: " + cssPath);
            System.out.println("CSS URL: " + cssUrl);
            if (cssUrl != null) {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("CSS file not found: " + cssPath);
            }
            
            stage.setScene(scene);
            
            // Устанавливаем окно в полноэкранный режим
            stage.setMaximized(true);
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setResizable(true);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
