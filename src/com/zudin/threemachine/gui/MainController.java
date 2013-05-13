package com.zudin.threemachine.gui;

import com.zudin.threemachine.methods.Methods;
import com.zudin.threemachine.model.Detail;
import com.zudin.threemachine.model.Way;
import com.zudin.threemachine.model.commands.ClearCommand;
import com.zudin.threemachine.model.commands.Command;
import com.zudin.threemachine.model.commands.LoadCommand;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    private static ArrayList<Detail> details = new ArrayList<>();
    private static Way result;
    private static Stack<Command> stackUndo = new Stack<>();
    private static Stack<Command> stackRedo = new Stack<>();

    @FXML
    private StackPane stackBottomProgress1;
    @FXML
    private StackPane stackBottomProgress2;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private MenuItem animationMark;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private StackPane stackPaneUndo;
    @FXML
    private StackPane stackPaneRedo;
    @FXML
    private GridPane grid;
    @FXML
    private TextField resultField;
    @FXML
    private HBox hbox;
    private String[] colors = new String[20];
    private String[] secColors = new String[20];
    private ArrayList<FadeTransition> transitions = new ArrayList<>();
    private File prevDirectory;
    private int recWidth = 15;
    private int recHeigth = 40;
    private boolean animationEnabled = true;

    public void initialize() {
        setColors();
        setDesign();
        setActions();
    }

    //Handlers

    public void handleSave(ActionEvent actionEvent) throws ParserConfigurationException, TransformerException {
        FileChooser fileChooser = FileChooserBuilder.create()
                .extensionFilters(new FileChooser.ExtensionFilter("XML Files", "*.xml"))
                .build();
        if (prevDirectory != null) {
            fileChooser.setInitialDirectory(prevDirectory.getParentFile());
        }
        File file = fileChooser.showSaveDialog(grid.getScene().getWindow());
        if (file == null) {
            return;
        }
        if (!file.getPath().endsWith(".xml")) {
            file = new File(file.getPath() + ".xml");
        }
        prevDirectory = file;
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element base = doc.createElement("base");
        doc.appendChild(base);
        for (Detail detail : details) {
            Element detElem = doc.createElement("detail");
            base.appendChild(detElem);
            detElem.setAttribute("I", String.valueOf(detail.getTimes()[0]));
            detElem.setAttribute("II", String.valueOf(detail.getTimes()[1]));
            detElem.setAttribute("III", String.valueOf(detail.getTimes()[2]));
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public void handleLoad(ActionEvent actionEvent) throws IOException {
        LoadCommand command = new LoadCommand(this, grid, resultField, prevDirectory);
        command.execute();
        addCommand(command);
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleClear(ActionEvent actionEvent) {
        if (!details.isEmpty()) {
            ClearCommand command = new ClearCommand(this, grid, resultField);
            command.execute();
            addCommand(command);
        }
    }

    public void handleManager (ActionEvent actionEvent) throws IOException {
        DetailsManagerController.setMc(this);
        DetailsManagerController.setDetails(details);
        Parent root = FXMLLoader.load(getClass().getResource("resources/templates/managerform.fxml"));
        Stage stage = StageBuilder.create()
                .title("Details Manager")
                .scene(new Scene(root))
                .minHeight(450)
                .minWidth(660)
                .icons(new Image("com/zudin/threemachine/gui/resources/images/icon.png"))
                .build();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(grid.getScene().getWindow());
        stage.show();
    }

    public void handleInstructions(ActionEvent actionEvent) {
        //TODO instructions
    }

    public void handleAbout(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("resources/templates/aboutform.fxml"));
        Stage stage = StageBuilder.create()
                .title("About")
                .scene(new Scene(root))
                .height(240)
                .width(300)
                .resizable(false)
                .icons(new Image("com/zudin/threemachine/gui/resources/images/icon.png"))
                .build();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(grid.getScene().getWindow());
        stage.show();
    }

    public void handleFullInfo(ActionEvent actionEvent) throws IOException {
        if (result == null || details.isEmpty()) {
            MessageController.showMessage(getClass(), grid.getScene().getWindow(), "Error", "There are not any details");
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("resources/templates/infoform.fxml"));
        Stage stage = StageBuilder.create()
                .title("Result information")
                .scene(new Scene(root))
                .minHeight(100)
                .minWidth(150)
                .icons(new Image("com/zudin/threemachine/gui/resources/images/icon.png"))
                .build();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(grid.getScene().getWindow());
        stage.show();
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

    public void handleAnimationMark(ActionEvent actionEvent) {
        if (animationEnabled) {
            animationEnabled = false;
            animationMark.setText("Enable animation");
        } else {
            animationEnabled = true;
            animationMark.setText("Disable animation");
        }
    }

    //Other methods

    public void solve() {
        progressIndicator.setVisible(true);
        Task solving = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                result = Methods.getSolution(details);
                return null;
            }
        };
        //result = Methods.getSolution(details);
        solving.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                progressIndicator.setVisible(false);
                resultField.setText("Your way is: " + result.toString());
                repaint();
            }
        });
        new Thread(solving).start();
//        progressIndicator.setVisible(false);
//        resultField.setText("Your way is: " + result.toString());
//        repaint();
    }

    private void setColors() {
        colors[0] = "#ef92d2";
        colors[1] = "#00cc21";
        colors[2] = "#ff9533";
        colors[3] = "#00a3ff";
        colors[4] = "#72cc00";
        colors[5] = "#ff007a";
        colors[6] = "#00ccb4";
        colors[7] = "#ff3333";
        colors[8] = "#fff500";
        colors[9] = "#d2ff99";
        colors[10] = "#eb99ff";
        colors[11] = "#d0ccff";
        colors[12] = "#ffd466";
        colors[13] = "#00EFC1";
        colors[14] = "#BD38EF";
        colors[15] = "#E5EFED";
        colors[16] = "#EF5E99";
        colors[17] = "#EF812A";
        colors[18] = "#ACC5EF";
        colors[19] = "#33EF19";
        secColors[0] = "#A9588B";
        secColors[1] = "#008C13";
        secColors[2] = "#BE6829";
        secColors[3] = "#007BB9";
        secColors[4] = "#458700";
        secColors[5] = "#B90068";
        secColors[6] = "#00927F";
        secColors[7] = "#B92424";
        secColors[8] = "#BAB000";
        secColors[9] = "#96BA6E";
        secColors[10] = "#A26DB5";
        secColors[11] = "#9793B8";
        secColors[12] = "#B89948";
        secColors[13] = "#00A27F";
        secColors[14] = "#8B24AB";
        secColors[15] = "#A4AEAC";
        secColors[16] = "#AB4070";
        secColors[17] = "#AE5C1E";
        secColors[18] = "#7E8CAD";
        secColors[19] = "#2AB216";
    }

    private void setDesign() {
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(10.0));
        hbox.setPadding(new Insets(3.5));
        stackPaneUndo.setAlignment(Pos.CENTER_RIGHT);
        stackPaneRedo.setAlignment(Pos.CENTER_RIGHT);
        stackBottomProgress1.setAlignment(Pos.CENTER_RIGHT);
        stackBottomProgress2.setAlignment(Pos.CENTER_RIGHT);
        progressIndicator.setVisible(false);
    }

    private void setActions() {
        Tooltip.install(undoButton, TooltipBuilder.create().text("Undo (Ctrl + Z)").styleClass("ttip").build());
        Tooltip.install(redoButton, TooltipBuilder.create().text("Redo (Ctrl + Y)").styleClass("ttip").build());
    }

    private void repaint() {
        if (result != null) {
            transitions.clear();
            grid.getChildren().clear();
            grid.add(LabelBuilder.create().textFill(Color.WHITE).text("I ").build(), 0, 0);
            grid.add(LabelBuilder.create().textFill(Color.WHITE).text("II ").build(), 0, 1);
            grid.add(LabelBuilder.create().textFill(Color.WHITE).text("III ").build(), 0, 2);
            HBox[] hBoxes = new HBox[3];
            for (int i = 0; i < 3; i ++) {
                hBoxes[i] = new HBox();
                grid.add(hBoxes[i], 1, i);
            }
            int[] shifts = new int[3];
            for (int i = 0; i < 3; i ++) {
                if (i > 0) {
                    shifts[i] += shifts[i - 1];
                    hBoxes[i].getChildren().add(getEmptyRectangle(shifts[i - 1]));
                }
                hBoxes[i].getChildren().add(getDetailsStackPane(result.getDetails().get(0).getTimes()[i],
                        result.getDetails().get(0).getId(), i));
                shifts[i] += result.getDetails().get(0).getTimes()[i];
            }
            for (int i = 1; i < result.getDetails().size(); i ++) {
                Detail detail = result.getDetails().get(i);
                hBoxes[0].getChildren().add(getDetailsStackPane(detail.getTimes()[0], detail.getId(), 0));
                shifts[0] += detail.getTimes()[0];
                for (int j = 1; j < detail.getTimes().length; j ++) {
                    int delta = shifts[j - 1] > shifts[j] ? shifts[j - 1] - shifts[j] : 0; //if there is a gap between times
                    if (delta > 0) {
                        hBoxes[j].getChildren().add(getEmptyRectangle(delta));
                    }
                    hBoxes[j].getChildren().add(getDetailsStackPane(detail.getTimes()[j], detail.getId(), j));
                    shifts[j] += delta + detail.getTimes()[j];
                }
            }
        }
        if (animationEnabled) {
            int shift = 0;
            for (final FadeTransition transition : transitions) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        transition.play();
                    }
                }, shift);
                shift += 100;
            }
        }
    }

    private StackPane getDetailsStackPane (final int width, final int id, final int numOfMachine) {
        Tooltip tooltip = TooltipBuilder.create()
                .text(String.valueOf(width))
                .styleClass("ttip")
                .build();
        Stop[] stops = new Stop[] {new Stop(0, Color.web(colors[id % 20])), new Stop(1, Color.web(secColors[id % 20]))};
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        final MainController mc = this;
        Rectangle rectangle = RectangleBuilder.create()
                .width(width * recWidth)
                .height(recHeigth)
                .fill(gradient)
                .strokeWidth(1)
                .stroke(Color.web("#5F5F5F"))
                .onMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            EditTimeController.setData(mc, width, numOfMachine, id);
                            Parent root = FXMLLoader.load(getClass().getResource("resources/templates/edittimeform.fxml"));
                            Stage stage = StageBuilder.create()
                                    .x(mouseEvent.getScreenX())
                                    .y(mouseEvent.getScreenY())
                                    .scene(new Scene(root))
                                    .height(55)
                                    .width(45)
                                    .resizable(false)
                                    .build();
                            stage.initModality(Modality.WINDOW_MODAL);
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.initOwner(grid.getScene().getWindow());
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .build();
        rectangle.heightProperty().bind(grid.getScene().heightProperty().divide(5));
        rectangle.widthProperty().bind(grid.getScene().widthProperty().divide(result.getTimes()[2] * 1.1).multiply(width));
        Tooltip.install(rectangle, tooltip);
        Text text = new Text(String.valueOf(id));
        text.setFont(new Font(16));
        final StackPane stackPane = StackPaneBuilder.create()
                .prefWidth(rectangle.getWidth())
                .prefHeight(rectangle.getHeight())
                .children(rectangle, text)
                .build();
        final Rectangle tempRect = rectangle;
        rectangle.heightProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                stackPane.setPrefHeight(tempRect.getHeight());
            }
        });
        rectangle.widthProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                stackPane.setPrefWidth(tempRect.getWidth());
            }
        });
        if (animationEnabled) {
            stackPane.setOpacity(0);
            transitions.add(FadeTransitionBuilder.create().node(stackPane).fromValue(0).toValue(1).build());
        }
        return stackPane;
    }

    private Rectangle getEmptyRectangle (int width) {
        Tooltip tooltip = TooltipBuilder.create()
                .text(String.valueOf(width))
                .styleClass("ttip")
                .build();
        Rectangle rectangle = RectangleBuilder.create()
                .width(width * recWidth)
                .height(recHeigth)
                .opacity(0)
                .build();
        rectangle.heightProperty().bind(grid.getScene().heightProperty().divide(5));
        rectangle.widthProperty().bind(grid.getScene().widthProperty().divide(result.getTimes()[2] * 1.1).multiply(width));
        Tooltip.install(rectangle, tooltip);
        return rectangle;
    }

    public void setPrevDirectory(File newPath) {
        prevDirectory = newPath;
    }

    public static void setDetails (ArrayList<Detail> d) {
        details.clear();
        details.addAll(d);
    }

    public static ArrayList<Detail> getDetails () {
        return details;
    }

    public static Way getResult() {
        return result;
    }

    public static void addCommand(Command command) {
        stackUndo.add(command);
        stackRedo.clear();
    }
}
