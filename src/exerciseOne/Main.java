package exerciseOne;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../sample/ui.fxml"));
        primaryStage.setTitle("Genetische Algorithmen");
        primaryStage.setScene(new Scene(root, 600, 590));
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);

        // int geneCnt = 200;
        // double initRate = 0.05;
        // int geneLen = 200;
        // int maxGenerations = 3000;
        // double startPc = 0.5;
        // double endPc = 0.9;
        // double stepPc = 0.02;
        // double startPm = 0;
        // double endPm = 0.03;
        // double stepPm = 0.002;
        // int replicationScheme = 2;
        // int crossOverMethod = 1;
        // int numberOfRunsToAverage = 10;
        // boolean protectBest = true;
        // double acceptRate = 1;
        // boolean initializeLikeCourse = false;
        // int numberOfThreads = 5;

        // new AllOneIsFittest(200, 0.05, 200, 3000, 0.9, 0.006, 1, 1, 10, true, 1, false);
        // new AllOneIsFittest(geneCnt, initRate, geneLen, maxGenerations, startPc, endPc, stepPc, startPm, endPm, stepPm, replicationScheme, crossOverMethod, numberOfRunsToAverage, protectBest, acceptRate, initializeLikeCourse, numberOfThreads);
    }

}