package ru.teosa.pokemonhelper.service;

import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.teosa.pokemonhelper.configuration.Properties;
import ru.teosa.pokemonhelper.utils.AppUtils;
import ru.teosa.pokemonhelper.utils.Logger;

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
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust timeout as needed
//        WebElement modalElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myModal")));

//        resetLocation();

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
        AppUtils.sleep(6000L);

        if (Properties.getInstance().isLimitOver(enemyCountField.getText())) {
            Logger.getInstance().log("Достигнут лимит врагов. Телепортация в город...");
        } else {
            Logger.getInstance().log("Ресурсы для боя окончены. Телепортация в город...");
        }

//        resetLocation();
    }

    public void heal() {
        // Нажимаем на клетку напротив двери
        Logger.getInstance().log("Заходим в ратушу...");
        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[312]")).click();
        AppUtils.sleep();

        // Нажимаем на появившуюся зеленую стрелку
        driver.findElement(By.className("exitbutton")).click();

        AppUtils.sleep();

        // Нажимаем на стойку с NPC
        Logger.getInstance().log("Лечим покемонов...");
        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[308]")).click();
        AppUtils.sleep();

        // Вызываем диалог с NPC
        driver.findElement(By.className("VoiceNpc")).click();
        AppUtils.sleep();

        // Получаем окно диалога
        var dialogWindow = driver.findElement(By.className("modal_npc_voice"));
        // Получаем элемент с ответами
        var answers = dialogWindow.findElement(By.className("Answer"));
        // Выбираем первый ответ из списка (Вылечить покемонов)
        answers.findElements(By.tagName("div")).get(0).click();

        AppUtils.sleep();

        // Идем обратно к двери
        Logger.getInstance().log("Выходим из ратуши...");
        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[316]")).click();

        AppUtils.sleep();

        // Нажимаем на пояявившуюся зеленую стрелку
        driver.findElement(By.className("exitbutton")).click();

        AppUtils.sleep();

        // Возвращаемся на стартовую позицию
        Logger.getInstance().log("Возврат на стартовую позицию...");
        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[313]")).click();

        AppUtils.sleep();
    }

    public void makeAutoButtle() {
        String battleWindowXpath = "//*[@id=\"battleMap\"]/div";
        boolean enoughSkillPointsQty = true;

        while (enoughSkillPointsQty && !Properties.getInstance().isLimitOver(enemyCountField.getText())) {
            Logger.getInstance().log("Активация поиска врагов...");
            driver.findElement(By.className("VoiceWild")).click();

            while (AppUtils.findByXPath(driver, battleWindowXpath) == null) {
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

    public void resetLocation() {
//        Logger.getInstance().log("Проверка местонахождения...");

        Logger.getInstance().log("Ресет локации. Открываем карту...");
        driver.findElement(By.className("mapBl")).click();

        AppUtils.sleep();

        Logger.getInstance().log("Выбираем Санталун...");
        try {
            driver.findElement(By.xpath("/html/body/div[19]/div[2]/div/div/div[8]")).click();
        } catch (Exception e) {
            Logger.getInstance().log("Выбираем Санталун2...");
            driver.findElement(By.className("loc_santalunecity")).click();
        }

        AppUtils.sleep();

        WebElement teleportationButton = AppUtils.findByXPath(driver, "/html/body/div[4]/div[5]");

        if (teleportationButton != null) {
            Logger.getInstance().log("Телепортируемся...");
            teleportationButton.click();

            AppUtils.sleep();
            Logger.getInstance().log("Персонаж успешно перемещен в Санталун");
        } else {
            Logger.getInstance().log("Персонаж в Санталун. Телепортация не требуется");
            driver.findElement(By.xpath("//*[@id=\"drgModel\"]/span/i")).click();
        }

    }

}
