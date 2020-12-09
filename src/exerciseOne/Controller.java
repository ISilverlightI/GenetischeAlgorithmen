package exerciseOne;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;

public class Controller {

    @FXML
    private TextField geneCnt;
    @FXML
    private TextField geneLen;
    @FXML
    private TextField initRate;
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
    //private static String text;

    @FXML
    public void normalStart() {
        int geneCnt = Integer.parseInt(this.geneCnt.getCharacters().toString());
        double initRate = Double.parseDouble(this.initRate.getCharacters().toString());
        int geneLen = Integer.parseInt(this.geneLen.getCharacters().toString());
        int maxGenerations = Integer.parseInt(this.maxGenerations.getCharacters().toString());
        double pc = Double.parseDouble(this.pc.getCharacters().toString());
        double pm = Double.parseDouble(this.pm.getCharacters().toString());
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
        double acceptRate = 1;
        boolean initializeLikeCourse = false;
        int s = Integer.parseInt(this.sValue.getCharacters().toString());

        if (!running) {
            setRunning(true);
            new AllOneIsFittest(geneCnt, initRate, geneLen, maxGenerations, pc, pm, replicationScheme, recombinationMethod, numberOfRunsToAverage, protectBest, acceptRate, initializeLikeCourse, s, progressBar, resultLabel);
        }
    }

    @FXML
    public void optimizationStart() {

        int geneCnt = Integer.parseInt(this.geneCnt.getCharacters().toString());
        double initRate = Double.parseDouble(this.initRate.getCharacters().toString());
        int geneLen = Integer.parseInt(this.geneLen.getCharacters().toString());
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
        double acceptRate = 1;
        boolean initializeLikeCourse = false;
        int s = Integer.parseInt(this.sValue.getCharacters().toString());

        if (!running) {
            setRunning(true);
            new AllOneIsFittest(geneCnt, initRate, geneLen, maxGenerations, startPc, endPc, stepPc, startPm, endPm, stepPm, replicationScheme, recombinationMethod, numberOfRunsToAverage, protectBest, acceptRate, initializeLikeCourse, numberOfThreads, s, progressBar, resultLabel);
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

    /*
        @FXML
        public void printResult() {
            this.resultLabel.setText(text);
        }

        public static void setText(String string) {
            text = string;
        }
    */
    public static void setRunning(boolean value) {
        running = value;
    }
}
