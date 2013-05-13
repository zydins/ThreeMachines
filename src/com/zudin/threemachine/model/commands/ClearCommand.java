package com.zudin.threemachine.model.commands;

import com.zudin.threemachine.gui.MainController;
import com.zudin.threemachine.model.Detail;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 04.05.13
 */
public class ClearCommand implements Command {
    private MainController mc;
    private GridPane grid;
    private TextField resultField;
    private ArrayList<Detail> oldDetails;

    public ClearCommand(MainController mc, GridPane grid, TextField resultField) {
        this.grid = grid;
        this.resultField = resultField;
        this.mc = mc;
    }

    /**
     * Execute the method
     */
    @Override
    public void execute() {
        oldDetails = new ArrayList<>(MainController.getDetails());
        resultField.setText("");
        grid.getChildren().clear();
        MainController.getDetails().clear();
        Detail.reset();
    }

    /**
     * Return previous condition
     */
    @Override
    public void undo() {
        MainController.setDetails(oldDetails);
        Detail.setCounter(oldDetails.size() + 1);
        mc.solve();
    }

    @Override
    public void redo() {
        execute();
    }
}
