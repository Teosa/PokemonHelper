package ru.teosa.pokemonhelper;

import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MovementService {

    private final WebDriver driver;

    private TextField enemyCountField;

    public MovementService(WebDriver driver) {
        this.driver = driver;
    }

    public void setEnemyCountField(TextField enemyCountField) {
        this.enemyCountField = enemyCountField;
        this.enemyCountField.setText(String.valueOf(0));
    }

    public void moveToFields() {
        resetLocation();

        Logger.getInstance().log("Идем к краю карты...");
        driver.findElement(By.xpath("/html/body/div[17]/div/div[1]/div[3]/div[469]")).click();

        String greenArrowXPath = "//*[@id=\"coord-layer\"]/div[625]";
        while (AppUtils.findByXPath(driver, greenArrowXPath) == null) {
            AppUtils.sleep();
        }

        Logger.getInstance().log("Смена локации...");
        driver.findElement(By.xpath(greenArrowXPath)).click();

        AppUtils.sleep();

        Logger.getInstance().log("Перемещение...");
        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[473]")).click();

        AppUtils.sleep(6000L);

        randomNpcButtle();

        Logger.getInstance().log("Перемещение окончено");

        makeAutoButtle();

        Logger.getInstance().log("Ресурсы для боя окончены. Телепортация в город...");
        resetLocation();
    }

    public void makeAutoButtle() {
        String battleWindowXpath = "//*[@id=\"battleMap\"]/div";
        boolean enoughSkillPointsQty = true;

        while (enoughSkillPointsQty) {
            Logger.getInstance().log("Активация поиска врагов...");
            driver.findElement(By.className("VoiceWild")).click();

            while (AppUtils.findByXPath(driver, battleWindowXpath) == null) {
                Logger.getInstance().log("Поиск противника...");
                AppUtils.sleep();
            }

            Logger.getInstance().log("Противник найден");
            driver.findElement(By.className("VoiceWild")).click();

            enoughSkillPointsQty = new BattleService(driver).battle();
            enemyCountField.setText(String.valueOf(Integer.parseInt(enemyCountField.getText()) + 1));
        }
    }

    private void randomNpcButtle() {
        Logger.getInstance().log("Проверка диалога с NPC...");
        var npcDialog = AppUtils.findByXPath(driver, "//*[@id=\"window_games\"]/div/div[4]");

        if (npcDialog != null) {
            Logger.getInstance().log("Диалог найден. Начало боя...");
            npcDialog.findElement(By.xpath("//*[@id=\"window_games\"]/div/div[3]/div[3]")).click();

            AppUtils.sleep();

            new BattleService(driver).battle();

            Logger.getInstance().log("Перемещение...");
            driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[469]")).click();
            AppUtils.sleep(6000L);
        }
    }

    private void resetLocation() {
        Logger.getInstance().log("Проверка местонахождения...");
        var grandma = AppUtils.findByXPath(driver, "//*[@id=\"coord-layer\"]/div[486]");

        if (grandma == null || !grandma.getAttribute("class").contains("activated npc")) {
            Logger.getInstance().log("Персонаж не в городе. Требуется ресет локации. Открываем карту...");
            driver.findElement(By.xpath("/html/body/div[15]/div[3]/div[2]/div[1]")).click();

            AppUtils.sleep();

            Logger.getInstance().log("Выбираем Санталун...");
            try {
                driver.findElement(By.xpath("/html/body/div[19]/div[2]/div/div/div[8]")).click();
            } catch (Exception e) {
                Logger.getInstance().log("Выбираем Санталун2...");
                driver.findElement(By.className("loc_santalunecity")).click();
            }

            AppUtils.sleep();

            Logger.getInstance().log("Телепортируемся...");
            driver.findElement(By.xpath("/html/body/div[4]/div[5]")).click();

            AppUtils.sleep();
            Logger.getInstance().log("Персонаж успешно перемещен в Санталун");
        }
    }

}
