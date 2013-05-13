package com.zudin.threemachine.model.commands;

import com.zudin.threemachine.gui.DetailsManagerController;
import com.zudin.threemachine.gui.model.AddButton;
import com.zudin.threemachine.gui.model.NodeGroup;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Zudin Sergey, 272(2)
 * 03.05.13
 */
public class DeleteCommand implements Command {
    private GridPane grid;
    private NodeGroup nodeGroup;
    private String[] times = new String[3];

    public DeleteCommand(GridPane grid, NodeGroup nodeGroup) {
        this.grid = grid;
        this.nodeGroup = nodeGroup;
        for (int i = 0; i < 3; i ++) {
            times[i] = nodeGroup.getFields()[i].getText();
        }
    }
    /**
     * Execute the method
     */
    @Override
    public void execute() {
        int index = NodeGroup.getGroups().indexOf(nodeGroup);
        int gridColumnIndex = GridPane.getColumnIndex(nodeGroup.getNumberLabel());
        int gridRowIndex = GridPane.getRowIndex(nodeGroup.getNumberLabel());
        //remove group
        grid.getChildren().remove(nodeGroup.getDeleteButton());
        grid.getChildren().remove(nodeGroup.getNumberLabel());
        for (int k = 0; k < 3; k++) {
            grid.getChildren().remove(nodeGroup.getFields()[k]);
        }
        NodeGroup.getGroups().remove(nodeGroup);
        //shift groups
        for (int i = index; i < NodeGroup.getGroups().size(); i++) {
            NodeGroup group = NodeGroup.getGroups().get(i);
            int nextColumnIndex = GridPane.getColumnIndex(group.getNumberLabel());
            int nextRowIndex = GridPane.getRowIndex(group.getNumberLabel());
            GridPane.setColumnIndex(group.getNumberLabel(), gridColumnIndex);
            GridPane.setColumnIndex(group.getDeleteButton(), gridColumnIndex + 1);
            GridPane.setRowIndex(group.getNumberLabel(), gridRowIndex);
            GridPane.setRowIndex(group.getDeleteButton(), gridRowIndex);
            for (int j = 0; j < 3; j++) {
                GridPane.setColumnIndex(group.getFields()[j], gridColumnIndex);
                GridPane.setColumnSpan(group.getFields()[j], 2);
                GridPane.setRowIndex(group.getFields()[j], ++gridRowIndex);
            }
            group.getNumberLabel().setText(String.valueOf(i + 1));
            gridColumnIndex = nextColumnIndex;
            gridRowIndex = nextRowIndex;
        }
        //shift addButton
        int correctRows = ((NodeGroup.getGroups().size() - 1) / DetailsManagerController.getColNumber() + 1) * 4;
        if (gridRowIndex >= correctRows) { //shift to the prev line
            Label[] labels = new Label[4];
            for (Node node : grid.getChildren()) {
                if (node instanceof Label) {
                    Label label = (Label)node;
                    switch (label.getText()) {
                        case "Number":
                            labels[0] = label;
                            break;
                        case "1 detail":
                            labels[1] = label;
                            break;
                        case "2 detail":
                            labels[2] = label;
                            break;
                        case "3 detail":
                            labels[3] = label;
                            break;
                    }
                }
            }
            for (Label label : labels) {
                grid.getChildren().remove(label);
            }
            GridPane.setColumnIndex(AddButton.getButton(), DetailsManagerController.getColNumber() * 2 + 1);
            GridPane.setRowIndex(AddButton.getButton(), gridRowIndex - 2);
        } else { //shift doesn't needed
            int col = GridPane.getColumnIndex(AddButton.getButton());
            GridPane.setColumnIndex(AddButton.getButton(), col - 2);
        }
        DetailsManagerController.changeCurrNumber(-1);
    }

    /**
     * Return previous condition
     */
    @Override
    public void undo() {
        new AddCommand(AddButton.getButton(), grid, times, nodeGroup).execute();
    }

    @Override
    public void redo() {
        execute();
    }
}
