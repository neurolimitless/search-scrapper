package view;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MainView extends Application {
    private Button buttonStart;
    private Button buttonShuffle;
    private TextField fieldSearchQuery;
    private ComboBox<String> boxSearchEngine;
    private TextField fieldPageCount;
    private Label labelProxyLoaded;
    private Label labelStatus;
    private RadioButton radioCsv;
    private RadioButton radioTxt;
    private ToggleGroup toggleGroup;
    private Controller controller;
    private CheckBox checkProxy;

    public Pane getPane() {
        buttonStart = new Button("Start");
        buttonShuffle = new Button("Shuffle proxies");
        labelProxyLoaded = new Label("Proxies loaded: " + controller.getCountOfProxies());
        fieldSearchQuery = getTextField("Search Query");
        boxSearchEngine = new ComboBox<>();
        fieldPageCount = getTextField("Pages to scrap");
        toggleGroup = new ToggleGroup();
        radioCsv = new RadioButton("csv");
        radioTxt = new RadioButton("txt");
        labelStatus = new Label("Ready");
        checkProxy = new CheckBox("Enable proxy");
        if (controller.getCountOfProxies() == 0) {
            checkProxy.setDisable(true);
            buttonShuffle.setDisable(true);
        }
        radioCsv.setToggleGroup(toggleGroup);
        radioTxt.setToggleGroup(toggleGroup);
        radioTxt.setSelected(true);
        boxSearchEngine.getItems().addAll("Google", "Bing", "Amazon", "Youtube", "Yahoo");
        boxSearchEngine.setValue(boxSearchEngine.getItems().get(0));
        boxSearchEngine.setPrefWidth(150);
        Pane pane = new FlowPane();
        pane.setPadding(new Insets(35));
        radioTxt.setPadding(new Insets(3));
        pane.getChildren().addAll(
                fieldSearchQuery, fieldPageCount, boxSearchEngine,
                radioCsv, radioTxt, labelProxyLoaded, checkProxy,
                buttonShuffle, buttonStart, labelStatus);
        buttonShuffle.setOnAction(event -> {
            controller.shuffleProxies();
            System.out.println("Proxy list shuffled.");
        });
        buttonStart.setOnAction(event -> {
            if (!textFieldIsEmpty(fieldSearchQuery.getText())) {
                if (isValidNumber(fieldPageCount.getText())) {
                    labelStatus.setText("Started.");
                    List<String> links = controller.launch(
                            fieldSearchQuery.getText(),
                            boxSearchEngine.getValue(),
                            fieldPageCount.getText(),
                            checkProxy.isSelected());
                    labelStatus.setText("Saving to file.");
                    controller.saveToFile(links,
                            selectFile(null, radioTxt.isSelected()),
                            radioTxt.isSelected());
                    labelStatus.setText("Finished.");
                } else labelStatus.setText("Enter valid number");
            } else labelStatus.setText("Enter query");
        });
        return pane;
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            controller = Controller.getInstance();
        } catch (Exception e) {
            checkProxy.setSelected(false);
            checkProxy.setDisable(true);
        }
        stage.setScene(new Scene(getPane()));
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("SearchEngineScrapper");
        stage.setWidth(240);
        stage.setHeight(300);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(MainView.class);
    }

    private TextField getTextField(String hintToSetup) {
        TextField field = new TextField();
        field.setPromptText(hintToSetup);
        field.setFocusTraversable(false);
        return field;
    }

    private boolean textFieldIsEmpty(String text) {
        return text.length() == 0;
    }

    private boolean isValidNumber(String text) {
        int invalidSymbols = 0;
        for (int i = 0; i < text.length(); i++) {
            if (!(Character.isDigit(text.charAt(i)) || text.charAt(i) == '-')) invalidSymbols++;
        }
        return invalidSymbols == 0;
    }

    private File selectFile(Stage stage, boolean isTxt) {
        FileChooser fileChooser = new FileChooser();
        String extension = isTxt ? "*.txt" : "*.csv";
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File with links", extension);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showSaveDialog(stage);
    }


}
