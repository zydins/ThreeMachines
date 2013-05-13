package com.zudin.threemachine.gui;

import com.zudin.threemachine.model.Detail;
import com.zudin.threemachine.model.Way;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 03.05.13
 */
public class InfoController {
    @FXML
    private TextArea textarea;
    @FXML
    private GridPane gridpane;

    public void initialize() {
        Way way = MainController.getResult();
        ArrayList<Detail> details = MainController.getDetails();
        gridpane.setPadding(new Insets(10));
        String text = "This is full version of solution for your details\n\nYour details:\n";
        String[] detailsRows = new String[4];
        detailsRows[0] = "Number\t\t";
        detailsRows[1] = "I machine\t\t";
        detailsRows[2] = "II machine\t";
        detailsRows[3] = "III machine\t";
        int[] downtime = new int[2];
        for (Detail detail : details) {
            detailsRows[0] += "#" + String.valueOf(detail.getId()) + "\t";
            detailsRows[1] += String.valueOf(detail.getTimes()[0]) + "\t";
            detailsRows[2] += String.valueOf(detail.getTimes()[1]) + "\t";
            detailsRows[3] += String.valueOf(detail.getTimes()[2]) + "\t";
            downtime[0] += detail.getTimes()[1];
            downtime[1] += detail.getTimes()[2];
        }
        for (int i = 0; i < 4; i ++) {
            text += detailsRows[i] + "\n";
        }
        downtime[0] = way.getTimes()[1] - downtime[0];
        downtime[1] = way.getTimes()[2] - downtime[1];
        text += "\n";
        text += "Your way is " + way.toString() + "\n";
        text += "The downtime of the first machine is 0\nThe downtime of the second machine is " + downtime[0] + "\n" +
                "The downtime of the third machine is " + + downtime[1] + "\n";
        textarea.setText(text);
    }
}
