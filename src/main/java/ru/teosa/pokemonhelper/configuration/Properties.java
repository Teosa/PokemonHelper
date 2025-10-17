package ru.teosa.pokemonhelper.configuration;

import lombok.Getter;

@Getter
public class Properties {

    private static Properties instance;

    private int targetEnemyQty;

    private Properties(String targetEnemyQty) {
        this.targetEnemyQty = Integer.parseInt(targetEnemyQty);
    }

    public static synchronized Properties getInstance() {
        if (instance == null) {
            throw new RuntimeException("Error: Properties not initialized");
        }

        return instance;
    }

    public static synchronized Properties getInstance(String targetEnemyQty) {
        if (instance == null) {
            instance = new Properties(targetEnemyQty);
        }

        instance.updateTargetQty(targetEnemyQty);
        return instance;
    }

    public synchronized boolean isLimitOver(String currentQty) {
        return targetEnemyQty == -1 ? false : Integer.parseInt(currentQty) >= this.targetEnemyQty;
    }

    public synchronized void updateTargetQty(String targetEnemyQty) {
        this.targetEnemyQty = Integer.parseInt(targetEnemyQty);
    }

}
