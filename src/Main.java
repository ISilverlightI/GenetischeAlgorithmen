import exerciseTwo.TravellingSalesman;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // startExerciseOne(primaryStage);
        startExerciseTwo();
    }

    public void startExerciseOne(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("exerciseOne/sample/ui.fxml"));
        primaryStage.setTitle("Genetische Algorithmen");
        primaryStage.setScene(new Scene(root, 600, 590));
        primaryStage.show();
    }

    private void startExerciseTwo() throws Exception {
        //new TravellingSalesman("06-map-100x100-200.txt", 100, 100000, 0.35, 0.4, 0.5, 0.01, 0.03, 0.5, 0, 1, 20, false, 1, 0, new ProgressBar(), new Label());
        new TravellingSalesman("06-map-100x100-200.txt", 100, 10000, 0.35, 0.02, 0, 1, 3, true, 0, new ProgressBar(), new Label());
    }

}