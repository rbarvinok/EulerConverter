package ua.cornerReflector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/view/sample.fxml";
        FXMLLoader loader = new FXMLLoader();

        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(getClass().getResource("/images/ang.png").toExternalForm()));
        stage.setTitle("CornerReflector");
        //stage.setScene(new Scene(root));
        stage.setScene(scene);
        stage.show();
    }
}