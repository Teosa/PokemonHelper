package ru.teosa.pokemonhelper;

import javafx.scene.control.TextArea;

public class Logger {

    private static Logger instance;

    private final TextArea myTextArea;

    private Logger(TextArea myTextArea) {
        this.myTextArea = myTextArea;
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            throw new RuntimeException("Error: logger not initialized by myTextArea");
        }

        return instance;
    }

    public static synchronized Logger getInstance(TextArea myTextArea) {
        if (instance == null) {
            instance = new Logger(myTextArea);
        }

        return instance;
    }

    public void log(String text) {
        myTextArea.appendText(AppUtils.getCurrentDateTimeFormated() + " - " + text + "\n");
    }

}
