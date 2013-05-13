package com.zudin.threemachine.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Zudin Sergey, 272(2)
 * 18.04.13
 */
public class MessageController {
    private static String message = "";
    public Label text;

    public void initialize() {
        text.setText(message);
    }

    public void handleOk(ActionEvent actionEvent) {
        Stage stage = (Stage) text.getScene().getWindow();
        stage.close();
    }

    public static void showMessage (Class cl, Window window, String title, String mes) throws IOException {
        message = mes;
        Parent root = FXMLLoader.load(cl.getResource("resources/templates/messagebox.fxml"));
        Stage stage = StageBuilder.create()
                .title(title)
                .scene(new Scene(root))
                .height(140)
                .width(269)
                .resizable(false)
                .icons(new Image("com/zudin/threemachine/gui/resources/images/icon.png"))
                .build();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(window);
        stage.show();
    }
}
