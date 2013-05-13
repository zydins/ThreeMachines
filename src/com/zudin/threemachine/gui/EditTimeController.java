package com.zudin.threemachine.gui;

import com.zudin.threemachine.model.commands.ChangeCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Zudin Sergey, 272(2)
 * 03.05.13
 */
public class EditTimeController {
    @FXML
    private TextField resultField;
    @FXML
    private GridPane gridpane;
    private static int value;
    private static int id;
    private static int numOfMachine;
    private static MainController mc;

    public void initialize() {
        gridpane.setAlignment(Pos.CENTER);
        resultField.setText(String.valueOf(value));
    }

    public void handleOK(ActionEvent actionEvent) {
        try {
            int temp = Integer.parseInt(resultField.getText());
            if (value < 1 || temp == value) return;
            ChangeCommand command = new ChangeCommand(mc, id, numOfMachine, value, temp);
            command.execute();
            MainController.addCommand(command);
        } finally {
            Stage stage = (Stage) resultField.getScene().getWindow();
            stage.close();
        }
    }

    public static void setData (MainController mc, int value, int numOfMachine, int id) {
        EditTimeController.mc = mc;
        EditTimeController.value = value;
        EditTimeController.numOfMachine = numOfMachine;
        EditTimeController.id = id;
    }
}
