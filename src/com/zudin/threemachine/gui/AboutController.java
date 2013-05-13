package com.zudin.threemachine.gui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

/**
 * Zudin Sergey, 272(2)
 * 30.04.13
 */
public class AboutController {
    @FXML
    private GridPane gridpane;

    public void initialize() {
        gridpane.setPadding(new Insets(15));
    }
}
