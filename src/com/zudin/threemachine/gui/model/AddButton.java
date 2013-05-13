package com.zudin.threemachine.gui.model;

import com.zudin.threemachine.gui.DetailsManagerController;
import com.zudin.threemachine.model.commands.AddCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Zudin Sergey, 272(2)
 * 30.04.13
 */

/**
 * Class represents special button for adding details on the DetailsManager scene
 */
public class AddButton extends Button {
    private static AddButton button = null;

    /**
     * Public constructor
     * @param grid GridPane of the scene
     */
    public AddButton (final GridPane grid) {
        button = this;
        setId("add");
        setPrefSize(20, 20);
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (DetailsManagerController.getCurrNumber() == 100) {
                    return;
                }
                AddCommand command = new AddCommand(button, grid);
                command.execute();
                DetailsManagerController.addCommand(command);
            }
        });
    }

    /**
     * Returns initialized AddButton
     * @return initialized AddButton
     */
    public static AddButton getButton () {
        return button;
    }
}
