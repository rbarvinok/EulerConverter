package ua.euler.javaclass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static ua.euler.controller.Controller.pressureNull;
import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.period;

public class GetSettings {
    public void getSettings() throws IOException {

        FileReader fileReader1 = new FileReader("settings.txt");
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);

        int lineNumber = 0;
        String line;
        while ((line = bufferedReader1.readLine()) != null) {

            if (lineNumber == 0) {
                pressureNull = Double.parseDouble(line.split("=")[1]);
            }
            if (lineNumber == 1) {
                period = Integer.parseInt(line.split("=")[1]);
            }
            lineNumber++;
        }
        fileReader1.close();
    }

}
