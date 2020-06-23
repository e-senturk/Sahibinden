package com.valinor61.sahibinden;

import com.valinor61.sahibinden.connection.Firefox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //Starts main window
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sahibinden.fxml"));
        primaryStage.setTitle("Sahibinden.com Fiyat Hesaplayıcı");
        primaryStage.setScene(new Scene(root, 1255, 685));
        primaryStage.setMinWidth(420);
        primaryStage.setMinHeight(420);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("logo.png"));
    }

    @Override
    public void stop() throws Exception {
        Firefox.destroyFirefox();
        super.stop();
        System.exit(0);
    }
}
