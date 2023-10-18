package com.group_2.ecommerceapplication;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication
        extends Application {
    @Override
    public void start(Stage stage) throws
            Exception {
        Font.loadFont(
                Objects.requireNonNull(MainApplication.class.getResource("fonts/Satisfy.ttf"))
                       .toExternalForm(), 10
        );
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
