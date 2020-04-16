package ua.euler.javaclass;

import javafx.fxml.FXML;



public class ProgressIndicatorRun {


    @FXML
    public void StartWork() throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
 //                   pi.setVisible(true);
                } catch (final Exception v) {
                }
            }
        }.start();
    }


    @FXML
    public void StopWork() throws Exception {
        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    pi.setVisible(false);
//                } catch (final Exception v) {
//                    // ...
//                }
//            }
        }.stop();
    }
}
