package main;

import control.RoleSelectionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        openWindow("../ui/RoleSelection.fxml", new RoleSelectionController());
    }

    public static void openWindow(String fxml, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        loader.setController(controller);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
