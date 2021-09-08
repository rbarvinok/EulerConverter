package ua.euler;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ua.euler.javaclass.servisClass.AlertAndInform;

import static ua.euler.controller.Controller.openFile;

public class Main extends Application {
    AlertAndInform inform = new AlertAndInform();
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/HAM.png").toExternalForm()));
        primaryStage.setTitle("Euler Converter  " + openFile);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event ->
        {
            inform.title = "Вихід з програми";
            inform.hd = "Закрити програму?";
            inform.ct = "Всі незбережені дані буде втрачено";
            inform.confirmation(event);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
