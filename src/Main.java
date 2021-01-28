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
        new TravellingSalesman("05-map-10x10-36border.txt", 100, 2000, 0, 0.9, 0.05, 0, 0.2, 0.005, 0, 1, 20, false, 14, 0, new ProgressBar(), new Label());
        //new TravellingSalesman("06-map-100x100-200.txt", 100, 2000, 0.8, 0.065, 1, 1, 50, true, 0, new ProgressBar(), new Label());
    }

}