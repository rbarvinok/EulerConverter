package ua.euler.javaclass;

import javafx.scene.control.TextField;

public class Clear {

    public static void clear(TextField txd, TextField txm, TextField txs, TextField tyd, TextField tym, TextField tys) {
        txd.setText("");
        txm.setText("");
        txs.setText("");
        tyd.setText("");
        tym.setText("");
        tys.setText("");
    }

    public static void clear(TextField txd, TextField txm, TextField txs, TextField tyd, TextField tym, TextField tys, TextField h) {
        txd.setText("");
        txm.setText("");
        txs.setText("");
        tyd.setText("");
        tym.setText("");
        tys.setText("");
        h.setText("");
    }

    public static void clear(TextField a, TextField d) {
        a.setText("");
        d.setText("");
    }

    public static void clear(TextField x, TextField y, TextField h) {
        x.setText("");
        y.setText("");
        h.setText("");
    }

    public static void clear(TextField x, TextField y,TextField h, TextField f) {
        x.setText("");
        y.setText("");
        h.setText("");
        f.setText("");
    }
}
