package exerciseTwo;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    @FXML
    private ComboBox map;
    @FXML
    private TextField geneCnt;
    @FXML
    private TextField maxGenerations;
    @FXML
    private TextField numberOfRunsToAverage;
    @FXML
    private RadioButton protectBest;
    @FXML
    private ComboBox recombinationMethod;
    @FXML
    private ComboBox replicationScheme;
    @FXML
    private TextField pc;
    @FXML
    private TextField pm;
    @FXML
    private TextField startPc;
    @FXML
    private TextField endPc;
    @FXML
    private TextField stepPc;
    @FXML
    private TextField startPm;
    @FXML
    private TextField endPm;
    @FXML
    private TextField stepPm;
    @FXML
    private TextField numberOfThreads;
    @FXML
    private Label sLabel;
    @FXML
    private TextField sValue;
    @FXML
    private Label resultLabel;
    @FXML
    private ProgressBar progressBar;

    private static boolean running = false;

    @FXML
    public void normalStart() throws Exception {
        String map = "05-map-10x10-36border.txt";
        if (this.map.getValue() != null) {
            switch (this.map.getValue().toString()) {
                case "05-map-10x10-36border.txt":
                    map = "05-map-10x10-36border.txt";
                    break;
                case "05-map-10x10-36-dist42.64.txt":
                    map = "05-map-10x10-36-dist42.64.txt";
                    break;
                case "06-map-100x100-50.txt":
                    map = "06-map-100x100-50.txt";
                    break;
            case "06-map-100x100-200.txt":
                    map = "06-map-100x100-200.txt";
                    break;
            case "07-map-500.txt":
                    map = "07-map-500.txt";
                    break;
            }
        } else {
            this.map.getSelectionModel().selectFirst();
        }
        int geneCnt = Integer.parseInt(this.geneCnt.getCharacters().toString());
        int maxGenerations = Integer.parseInt(this.maxGenerations.getCharacters().toString());
        double pc = Double.parseDouble(this.pc.getCharacters().toString());
        double pm = Double.parseDouble(this.pm.getCharacters().toString());
        int replicationScheme = 1;
        if (this.replicationScheme.getValue() != null) {
            switch (this.replicationScheme.getValue().toString()) {
                case "50x2":
                    replicationScheme = 0;
                    break;
                case "double best 50":
                    replicationScheme = 1;
                    break;
            }
        } else {
            this.replicationScheme.getSelectionModel().selectFirst();
        }
        int recombinationMethod = 1;
        if (this.recombinationMethod.getValue() != null) {
            switch (this.recombinationMethod.getValue().toString()) {
                case "greedy-Crossover":
                    recombinationMethod = 1;
                    break;
            }
        } else {
            this.recombinationMethod.getSelectionModel().selectFirst();
        }
        int numberOfRunsToAverage = Integer.parseInt(this.numberOfRunsToAverage.getCharacters().toString());
        boolean protectBest = this.protectBest.isSelected();

        System.out.println("\nStarting: " + map + " -Map");

        if (!running) {
            setRunning(true);
            new TravellingSalesman(map, geneCnt, maxGenerations, pc, pm, replicationScheme, recombinationMethod, numberOfRunsToAverage, protectBest, 0, progressBar, resultLabel);
        }
    }

    @FXML
    public void optimizationStart() throws Exception {
        String map = "05-map-10x10-36border.txt";
        if (this.map.getValue() != null) {
            switch (this.map.getValue().toString()) {
                case "05-map-10x10-36border.txt":
                    map = "05-map-10x10-36border.txt";
                    break;
                case "05-map-10x10-36-dist42.64.txt":
                    map = "05-map-10x10-36-dist42.64.txt";
                    break;
                case "06-map-100x100-50.txt":
                    map = "06-map-100x100-50.txt";
                    break;
                case "06-map-100x100-200.txt":
                    map = "06-map-100x100-200.txt";
                    break;
                case "07-map-500.txt":
                    map = "07-map-500.txt";
                    break;
            }
        } else {
            this.map.getSelectionModel().selectFirst();
        }
        int geneCnt = Integer.parseInt(this.geneCnt.getCharacters().toString());
        int maxGenerations = Integer.parseInt(this.maxGenerations.getCharacters().toString());
        double startPc = Double.parseDouble(this.startPc.getCharacters().toString());
        double endPc = Double.parseDouble(this.endPc.getCharacters().toString());
        double stepPc = Double.parseDouble(this.stepPc.getCharacters().toString());
        double startPm = Double.parseDouble(this.startPm.getCharacters().toString());
        double endPm = Double.parseDouble(this.endPm.getCharacters().toString());
        double stepPm = Double.parseDouble(this.stepPm.getCharacters().toString());
        int numberOfThreads = Integer.parseInt(this.numberOfThreads.getCharacters().toString());
        int replicationScheme = 1;
        if (this.replicationScheme.getValue() != null) {
            switch (this.replicationScheme.getValue().toString()) {
                case "50x2":
                    replicationScheme = 1;
                    break;
                case "Rank based selection":
                    replicationScheme = 2;
                    break;
            }
        } else {
            this.replicationScheme.getSelectionModel().selectFirst();
        }
        int recombinationMethod = 1;
        if (this.recombinationMethod.getValue() != null) {
            switch (this.recombinationMethod.getValue().toString()) {
                case "Crossover":
                    recombinationMethod = 1;
                    break;
                case "Front-rear":
                    recombinationMethod = 2;
                    break;
            }
        } else {
            this.recombinationMethod.getSelectionModel().selectFirst();
        }
        int numberOfRunsToAverage = Integer.parseInt(this.numberOfRunsToAverage.getCharacters().toString());
        boolean protectBest = this.protectBest.isSelected();

        if (!running) {
            setRunning(true);
            new TravellingSalesman(map, geneCnt, maxGenerations, startPc, endPc, stepPc, startPm, endPm, stepPm, replicationScheme, recombinationMethod, numberOfRunsToAverage, protectBest, numberOfThreads, 0, progressBar, resultLabel);
        }
    }

    @FXML
    public void showS() {
        if (this.replicationScheme.getValue() != null && this.replicationScheme.getValue().toString().equals("Rank based selection")) {
            sLabel.setVisible(true);
            sValue.setVisible(true);
        } else {
            sLabel.setVisible(false);
            sValue.setVisible(false);
        }
    }

    public static void setRunning(boolean value) {
        running = value;
    }
}
