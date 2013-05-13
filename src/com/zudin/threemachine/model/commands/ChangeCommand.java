package com.zudin.threemachine.model.commands;

import com.zudin.threemachine.gui.MainController;

/**
 * Zudin Sergey, 272(2)
 * 12.05.13
 */
public class ChangeCommand implements Command {
    private int value;
    private int oldValue;
    private int id;
    private int numOfMachine;
    private MainController mc;

    public ChangeCommand (MainController mc, int id, int numOfMachine, int oldValue, int value) {
        this.mc = mc;
        this.id = id;
        this.numOfMachine = numOfMachine;
        this.oldValue = oldValue;
        this.value = value;
    }

    @Override
    public void execute() {
        int[] times = MainController.getDetails().get(id - 1).getTimes();
        times[numOfMachine] = value;
        mc.solve();
    }

    /**
     * Return previous condition
     */
    @Override
    public void undo() {
        int[] times = MainController.getDetails().get(id - 1).getTimes();
        times[numOfMachine] = oldValue;
        mc.solve();
    }

    @Override
    public void redo() {
        execute();
    }
}
