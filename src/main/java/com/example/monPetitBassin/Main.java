package com.example.monPetitBassin;

import com.example.monPetitBassin.controllersFx.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Image icon = new Image(Main.class.getResource("images/icon.png").openStream());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.onLoad();
        Scene scene = new Scene(root, 1900, 1000);
        stage.setResizable(false);
        stage.setTitle("Ma petit bassin !");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        // fenêtre en plein écran
        // stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
