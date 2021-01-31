import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // startExerciseOne(primaryStage);
        startExerciseTwo(primaryStage);
    }

    public void startExerciseOne(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("exerciseOne/sample/ui.fxml"));
        primaryStage.setTitle("Genetische Algorithmen");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    private void startExerciseTwo(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("exerciseTwo/sample/ui.fxml"));
        primaryStage.setTitle("Genetische Algorithmen");
        primaryStage.setScene(new Scene(root, 600, 640));
        primaryStage.show();
    }

}