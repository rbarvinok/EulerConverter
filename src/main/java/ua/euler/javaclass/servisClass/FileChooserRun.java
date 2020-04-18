package ua.euler.javaclass.servisClass;

import javafx.stage.Stage;

import java.io.File;

public class FileChooserRun {

    Dovidka pb = new Dovidka();
    public static File selectedOpenFile;

    public void openFileChooser() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("EulerConverter. Відкриття файлу");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("*.csv", "*.csv"),
                new javafx.stage.FileChooser.ExtensionFilter(".txt", "*.txt"),
                new javafx.stage.FileChooser.ExtensionFilter("*.*", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            selectedOpenFile = selectedFile;
        } else {
            pb.hd = "Помилка! ";
            pb.ct = "Невдалось відкрити файл\n";
            pb.alert();
        }
    }

}
