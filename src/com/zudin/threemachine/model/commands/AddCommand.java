package com.zudin.threemachine.model.commands;

import com.zudin.threemachine.gui.DetailsManagerController;
import com.zudin.threemachine.gui.model.AddButton;
import com.zudin.threemachine.gui.model.NodeGroup;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Zudin Sergey, 272(2)
 * 03.05.13
 */
public class AddCommand implements Command {
    private AddButton button;
    private GridPane grid;
    private NodeGroup group;
    private String[] times;

    public AddCommand(AddButton button, GridPane grid) {
        this.grid = grid;
        this.button = button;
    }

    protected AddCommand(AddButton button, GridPane grid, String[] times, NodeGroup nodeGroup) {
        this.grid = grid;
        this.button = button;
        this.times = times;
        this.group = nodeGroup;
    }
    /**
     * Execute the method
     */
    @Override
    public void execute() {
        int col = GridPane.getColumnIndex(button);
        int row = GridPane.getRowIndex(button);
        if (group == null) {
            group = new NodeGroup(grid);
        } else {
            NodeGroup.addGroup(group);
        }
        if (times != null) {
            for (int i = 0; i < 3; i ++) {
                group.getFields()[i].setText(times[i]);
            }
        }
        if (col < DetailsManagerController.getColNumber() * 2 + 1) { //situation when shift doesn't needed
            GridPane.setColumnIndex(button, col + 2);
            GridPane.setColumnSpan(button, 2);
            grid.add(group.getNumberLabel(), col, row / 4 * 4);
            grid.add(group.getDeleteButton(), col + 1, row / 4 * 4);
            grid.add(group.getFields()[0], col, row / 4 * 4 + 1, 2, 1);
            grid.add(group.getFields()[1], col, row / 4 * 4 + 2, 2, 1);
            grid.add(group.getFields()[2], col, row / 4 * 4 + 3, 2, 1);
        } else { //shift to the next line
            grid.addColumn(0, LabelBuilder.create().text("Number").textFill(Color.WHITE).build(),
                    LabelBuilder.create().text("1 detail").textFill(Color.WHITE).build(),
                    LabelBuilder.create().text("2 detail").textFill(Color.WHITE).build(),
                    LabelBuilder.create().text("3 detail").textFill(Color.WHITE).build());
            grid.add(group.getNumberLabel(), 1, row - row % 4 + 4);
            grid.add(group.getDeleteButton(), 2, row - row % 4 + 4);
            grid.add(group.getFields()[0], 1, row - row % 4 + 4 + 1, 2, 1);
            grid.add(group.getFields()[1], 1, row - row % 4 + 4 + 2, 2, 1);
            grid.add(group.getFields()[2], 1, row - row % 4 + 4 + 3, 2, 1);
            GridPane.setColumnIndex(button, 3);
            GridPane.setRowIndex(button, row - row % 4 + 6);
            GridPane.setColumnSpan(button, 2);
        }
        DetailsManagerController.changeCurrNumber(1);
    }

    /**
     * Return previous condition
     */
    @Override
    public void undo() {
        new DeleteCommand(grid, group).execute();
    }

    @Override
    public void redo() {
        execute();
    }
}
