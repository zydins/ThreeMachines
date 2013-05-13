package com.zudin.threemachine.gui;

import com.zudin.threemachine.gui.model.NodeGroup;
import com.zudin.threemachine.model.Detail;
import com.zudin.threemachine.model.commands.Command;
import com.zudin.threemachine.model.commands.CreateCommand;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Zudin Sergey, 272(2)
 * 18.04.13
 */
public class DetailsManagerController {
    @FXML
    private StackPane stack1;
    @FXML
    private StackPane stack2;
    @FXML
    private HBox hbox;
    @FXML
    private TextField numberField;
    @FXML
//    private Label message;
    private Text message;
    @FXML
    private GridPane grid;
    @FXML
    private Button bottonButton;
    @FXML
    private BorderPane borderpane;

    private static MainController mc = null;
    private static ArrayList<Detail> details = new ArrayList<Detail>();
    private static int colNumber = 10;
    private static int currNumber = 0;
    private static Stack<Command> stackUndo = new Stack<>();
    private static Stack<Command> stackRedo = new Stack<>();

    public void initialize() throws Exception {
        BorderPane.setMargin(hbox, new Insets(10.0));
        BorderPane.setMargin(bottonButton, new Insets(10.0));
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setAlignment(Pos.CENTER);
        stack1.setAlignment(Pos.CENTER_RIGHT);
        stack2.setAlignment(Pos.CENTER_RIGHT);
        //message.setFont(new Font(14));
        message.setId("messagelabel");
        if (!details.isEmpty()) {
            numberField.setText(String.valueOf(details.size()));
            handleCreate(null);
            for (int i = 0; i < details.size(); i ++) {
                Detail detail = details.get(i);
                NodeGroup group = NodeGroup.getGroups().get(i);
                for (int j = 0; j < 3; j ++) {
                    group.getFields()[j].setText(String.valueOf(detail.getTimes()[j]));
                }
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                grid.getScene().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (event.isControlDown()) {
                            if (event.getCode() == KeyCode.valueOf("Z")) {
                                event.consume();
                                try {
                                    handleUndo(null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (event.getCode() == KeyCode.valueOf("Y")) {
                                event.consume();
                                try {
                                    handleRedo(null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public void handleCreate(ActionEvent actionEvent) throws Exception {
        try {
            int value = Integer.parseInt(numberField.getText());
            if (value > 100) {
                message.setText("Sorry, enter a number between 0 and 100");
                return;
            }
            currNumber = value;
            message.setText("");
            if (value <= 30) {
                colNumber = 10;
                try {
                    Stage stage = (Stage) borderpane.getScene().getWindow();
                    stage.setMinHeight(450);
                    stage.setMinWidth(660);
                } catch (Exception e) {} //this is the first run of scene
            } else {
                colNumber = 20;
                try {
                    Stage stage = (Stage) borderpane.getScene().getWindow();
                    stage.setMinHeight(450);
                    stage.setMinWidth(1300);
                } catch (Exception e) {} //this is the first run of scene
            }
//            createGrid(value);
            CreateCommand command = new CreateCommand(grid, value);
            command.execute();
            addCommand(command);
        } catch (NumberFormatException e) {
            message.setText("Try to enter a number");
        } catch (Exception e) {
            MessageController.showMessage(getClass(), grid.getScene().getWindow(), "Error", "Unexpected error!");
        }
    }

    public void handleSubmit(ActionEvent actionEvent) throws IOException {
        try {
            if (NodeGroup.getGroups().isEmpty()) {
                message.setText("You should add at least one detail");
                return;
            }
            ArrayList<Detail> tempDetails = new ArrayList<>();
            Detail.reset();
            for (NodeGroup group : NodeGroup.getGroups()) {
                int[] times = new int[3];
                for (int i = 0; i < 3; i ++) {
                    times[i] = Integer.parseInt((group.getFields()[i]).getText());
                    if (times[i] < 1) {
                        throw new NumberFormatException();
                    }
                }
                tempDetails.add(new Detail(times));
            }
            MainController.setDetails(tempDetails);
            details.clear();
            details.addAll(tempDetails);
            mc.solve();
            Stage stage = (Stage) borderpane.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            message.setText("Please fill all inputs positive numbers");
        } catch (Exception e) {
            MessageController.showMessage(getClass(), grid.getScene().getWindow(), "Error", "Unexpected error!");
        }
    }

    public void handleUndo(ActionEvent actionEvent) {
        if (!stackUndo.isEmpty()) {
            Command command = stackUndo.pop();
            command.undo();
            stackRedo.push(command);
        }
    }

    public void handleRedo(ActionEvent actionEvent) {
        if (!stackRedo.isEmpty()) {
            Command command = stackRedo.pop();
            command.redo();
            stackUndo.push(command);
        }
    }

    public static void setMc(MainController mc) {
        DetailsManagerController.mc = mc;
    }

    public static void setDetails(ArrayList<Detail> details) {
        DetailsManagerController.details = details;
    }

    public static int getColNumber() {
        return colNumber;
    }

    public static int getCurrNumber() {
        return currNumber;
    }

    public static void changeCurrNumber(int delta) {
        currNumber += delta;
    }

    public static void setCurrNumber(int value) {
        currNumber = value;
    }

    public static void addCommand(Command command) {
        stackUndo.add(command);
        stackRedo.clear();
    }
}

