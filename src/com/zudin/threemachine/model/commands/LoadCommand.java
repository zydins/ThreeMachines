package com.zudin.threemachine.model.commands;

import com.zudin.threemachine.gui.MainController;
import com.zudin.threemachine.gui.MessageController;
import com.zudin.threemachine.model.Detail;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooserBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Zudin Sergey, 272(2)
 * 05.05.13
 */
public class LoadCommand implements Command {
    private MainController mc;
    private GridPane grid;
    private File prevDirectory;
    private TextField resultField;
    private ArrayList<Detail> oldDetails;
    private ArrayList<Detail> newDetails;

    public LoadCommand(MainController mc, GridPane grid, TextField resultField, File prevDirectory) {
        this.mc = mc;
        this.grid = grid;
        this.resultField = resultField;
        this.prevDirectory = prevDirectory;
    }

    /**
     * Execute the method
     */
    @Override
    public void execute() {
        try {
            oldDetails = new ArrayList<>(MainController.getDetails());
            FileChooser fileChooser = FileChooserBuilder.create()
                    .extensionFilters(new FileChooser.ExtensionFilter("XML Files", "*.xml"))
                    .build();
            if (prevDirectory != null) {
                fileChooser.setInitialDirectory(prevDirectory.getParentFile());
            }
            File file = fileChooser.showOpenDialog(grid.getScene().getWindow());
            if (file == null) {
                return;
            }
            mc.setPrevDirectory(file);
            new ClearCommand(mc, grid, resultField).execute();
            ArrayList<Detail> tempDetails = new ArrayList<>();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("detail");
            for (int i = 0; i < list.getLength(); i ++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    tempDetails.add(new Detail(Integer.parseInt(element.getAttribute("I")),
                            Integer.parseInt(element.getAttribute("II")),
                            Integer.parseInt(element.getAttribute("III"))));
                }
            }
            MainController.getDetails().addAll(tempDetails);
            newDetails = tempDetails;
            mc.solve();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            try {
                MessageController.showMessage(mc.getClass(), grid.getScene().getWindow(), "Error", "Something going wrong, sorry");
            } catch (Exception ee) {e.printStackTrace();}
        } catch (Exception e) {
            try {
                new ClearCommand(mc, grid, resultField).execute();
                MessageController.showMessage(mc.getClass(), grid.getScene().getWindow(), "Error", "Incorrect file!");
            } catch (Exception ee) {e.printStackTrace();}
        }
    }

    /**
     * Return previous condition
     */
    @Override
    public void undo() {
        MainController.setDetails(oldDetails);
        Detail.setCounter(oldDetails.size() + 1);
        if (!oldDetails.isEmpty()) {
            mc.solve();
        } else {
            new ClearCommand(mc, grid, resultField).execute();
        }
    }

    @Override
    public void redo() {
        if (newDetails != null) {
            MainController.setDetails(newDetails);
            Detail.setCounter(newDetails.size() + 1);
            mc.solve();
        } else {
            new ClearCommand(mc, grid, resultField).execute();
        }
    }
}
