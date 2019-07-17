package ua.corner.javaclass;

import javafx.scene.control.Alert;

public class Dovidka {

    public String hd,ct;


        public void dovButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Довідка");
        alert.setHeaderText(hd);
        alert.setContentText(ct);
        alert.showAndWait();
              }

    public void alert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Помилка");
        alert.setHeaderText("Невірний формат даних");
        alert.setContentText("Поля для вводу \n - не можуть бути пустими; \n - повинні містити тільки цифри; \n - кут не може бути більшим 9,5 градусів; \n - відстань не може бути більшою за 270 км.");
        alert.showAndWait();
    }



}
