package com.zudin.threemachine.gui.model;

import com.zudin.threemachine.gui.DetailsManagerController;
import com.zudin.threemachine.model.commands.Command;
import com.zudin.threemachine.model.commands.DeleteCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 24.04.13
 */

/**
 * Class represents group of nodes used for holding and changing information about details
 */
public class NodeGroup {
   // private static int counter = 1;
    private static ArrayList<NodeGroup> groups = new ArrayList<NodeGroup>();

    private Label numberLabel;
    private Button deleteButton;
    private TextField[] fields = new TextField[3];

    /**
     * Public constructor
     * @param grid GridPane of the scene
     */
    public NodeGroup (final GridPane grid) {
        getGroups().add(this);
        numberLabel = new Label(String.valueOf(groups.size()));
        getNumberLabel().setTextFill(Color.WHITE);
        for (int i = 0; i < 3; i ++) {
            getFields()[i] = TextFieldBuilder.create().prefWidth(40).build();
        }
        final NodeGroup temp = this;
        deleteButton = ButtonBuilder.create()
                .text("x")
                .prefWidth(5)
                .prefHeight(5)
                .id("close")
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        DeleteCommand command = new DeleteCommand(grid, temp);
                        command.execute();
                        DetailsManagerController.addCommand(command);
                    }
                })
                .build();
    }

    /**
     * Returns ArrayList with all groups
     * @return list with groups
     */
    public static ArrayList<NodeGroup> getGroups() {
        return groups;
    }

    /**
     * Returns label which contains number of group
     * @return label with number of group
     */
    public Label getNumberLabel() {
        return numberLabel;
    }

    /**
     * Returns button which delete the group
     * @return delete button
     */
    public Button getDeleteButton() {
        return deleteButton;
    }

    /**
     * Returns textfiels with times of details
     * @return textfields
     */
    public TextField[] getFields() {
        return fields;
    }

    /**
     * Reset the counter to 0 and delete all groups
     */
    public static void resetGroups() {
        groups.clear();
    }

    public static void setGroups(ArrayList<NodeGroup> groups) {
        NodeGroup.groups = groups;
    }

    public static void addGroup(NodeGroup group) {
        if (!groups.contains(group))
            groups.add(group);
    }
}
