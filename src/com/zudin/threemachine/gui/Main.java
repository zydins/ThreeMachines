package com.zudin.threemachine.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    //TODO edit all mistakes (MainController, static methods...) <instance method of MC?>
    //TODO optimisation
    //TODO style for buttons
    //TODO style for change window
    //TODO check error with detail manager

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        Parent root = FXMLLoader.load(getClass().getResource("resources/templates/mainform.fxml"));
        primaryStage.setTitle("3 machine");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("resources/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);
        primaryStage.getIcons().add(new Image("com/zudin/threemachine/gui/resources/images/icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
