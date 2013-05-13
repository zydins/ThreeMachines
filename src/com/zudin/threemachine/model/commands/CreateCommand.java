package com.zudin.threemachine.model.commands;

import com.zudin.threemachine.gui.DetailsManagerController;
import com.zudin.threemachine.gui.model.AddButton;
import com.zudin.threemachine.gui.model.NodeGroup;
import javafx.scene.Node;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 04.05.13
 */
public class CreateCommand implements Command {
    private GridPane grid;
    private int value;
    private ArrayList<Node> oldChildren;
    private ArrayList<Node> newChildren;
    private int oldCurrNumber;
    private int newCurrNumber;
    private ArrayList<NodeGroup> oldGroups;
    private ArrayList<NodeGroup> newGroups;

    public CreateCommand(GridPane grid, int value) {
        this.grid = grid;
        oldChildren = new ArrayList<>(grid.getChildren());
        this.value = value;
        oldCurrNumber = DetailsManagerController.getCurrNumber();
        oldGroups = new ArrayList<>(NodeGroup.getGroups());
    }
    /**
     * Execute the method
     */
    @Override
    public void execute() {
        int cols = value < DetailsManagerController.getColNumber() ? value : DetailsManagerController.getColNumber();
        int rows = ((value - 1) / DetailsManagerController.getColNumber() + 1) * 4;
        grid.getChildren().clear();
        NodeGroup.resetGroups();
        int i, j = 0;
        for (i = 0; i < rows; i += 4) {
            grid.addColumn(0, LabelBuilder.create().text("Number").textFill(Color.WHITE).build(),
                    LabelBuilder.create().text("1 detail").textFill(Color.WHITE).build(),
                    LabelBuilder.create().text("2 detail").textFill(Color.WHITE).build(),
                    LabelBuilder.create().text("3 detail").textFill(Color.WHITE).build());
            for (j = 1; j < cols + 1 && (i / 4) * DetailsManagerController.getColNumber() + j - 1 < value; j ++) {
                addNodeGroup(new NodeGroup(grid), j * 2 - 1, i);
            }
        }
        AddButton addButton = new AddButton(grid);
        grid.add(addButton, j * 2 - 1, i - 2, 2, 1);
    }

    private void addNodeGroup (NodeGroup group, int col, int row) {
        grid.add(group.getNumberLabel(), col, row);
        grid.add(group.getDeleteButton(), col + 1, row);
        grid.add(group.getFields()[0], col, row + 1, 2, 1);
        grid.add(group.getFields()[1], col, row + 2, 2, 1);
        grid.add(group.getFields()[2], col, row + 3, 2, 1);
    }

    /**
     * Return previous condition
     */
    @Override
    public void undo() {
        newChildren = new ArrayList<>(grid.getChildren());
        newCurrNumber = DetailsManagerController.getCurrNumber();
        newGroups = new ArrayList<>(NodeGroup.getGroups());
        grid.getChildren().clear();
        DetailsManagerController.setCurrNumber(oldCurrNumber);
        NodeGroup.setGroups(oldGroups);
        grid.getChildren().addAll(oldChildren);
    }

    @Override
    public void redo() {
        grid.getChildren().clear();
        DetailsManagerController.setCurrNumber(newCurrNumber);
        NodeGroup.setGroups(newGroups);
        grid.getChildren().addAll(newChildren);
    }
}

