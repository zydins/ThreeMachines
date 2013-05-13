package com.zudin.threemachine.model.commands;

/**
 * Zudin Sergey, 272(2)
 * 03.05.13
 */
public interface Command {
    /**
     * Execute the method
     */
    abstract public void execute();

    /**
     * Return previous condition
     */
    abstract public void undo();

    abstract public void redo();

}
