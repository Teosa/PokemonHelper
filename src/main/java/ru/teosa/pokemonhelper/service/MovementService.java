package ru.teosa.pokemonhelper.service;

import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.teosa.pokemonhelper.Location;
import ru.teosa.pokemonhelper.configuration.Properties;
import ru.teosa.pokemonhelper.utils.AppUtils;
import ru.teosa.pokemonhelper.utils.Logger;

import java.time.Duration;

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

    public void work() {
        makeAutoButtle();
        AppUtils.sleep(6000L);

        if (Properties.getInstance().isLimitOver(enemyCountField.getText())) {
            Logger.getInstance().log("Достигнут лимит врагов. Телепортация в город...");
        } else {
            Logger.getInstance().log("Ресурсы для боя окончены. Телепортация в город...");
        }
    }

    @Deprecated
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

    private void move(int top, int left) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Находим элемент
        WebElement element = driver.findElement(By.id("active-pointer"));

//        System.out.println(element.getAttribute("outerHTML"));

        String style = """
                arguments[0].style.display = 'block';
                arguments[0].style.visibility = 'visible';
                arguments[0].style.opacity = '1';
                arguments[0].style.top = '{Y}px';
                arguments[0].style.left = '{X}px';
                arguments[0].style.pointerEvents = 'auto';
                """
                .replace("{X}", String.valueOf(left))
                .replace("{Y}", String.valueOf(top));

        // Устанавливаем координаты и делаем кликабельным
        js.executeScript(style, element);
//        System.out.println(element.getAttribute("outerHTML"));

        Actions actions = new Actions(driver);
        actions
                .moveToElement(element)
                .pause(Duration.ofMillis(500))
                .click()
                .perform();
//        System.out.println(element.getAttribute("outerHTML"));
        AppUtils.sleep();

        js.executeScript("""
                    arguments[0].style.pointerEvents = 'none';
                    arguments[0].style.display = 'none';
                """, element);
    }

    public void heal(Location location) {
        Logger.getInstance().log("Заходим в ратушу...");
        move(
                location.getCityCenterDoor().getTop(),
                location.getCityCenterDoor().getLeft());

        // Нажимаем на появившуюся зеленую стрелку
        driver.findElement(By.className("exitbutton")).click();
        AppUtils.sleep();

        // Нажимаем на стойку с NPC
        Logger.getInstance().log("Лечим покемонов...");
        move(128, 224);
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
        move(288, 224);

        AppUtils.sleep();

        // Нажимаем на пояявившуюся зеленую стрелку
        driver.findElement(By.className("exitbutton")).click();

        AppUtils.sleep();

        // Возвращаемся на стартовую позицию
        Logger.getInstance().log("Возврат на стартовую позицию...");
        move(
                location.getStartPosition().getTop(),
                location.getStartPosition().getLeft());

        AppUtils.sleep();

// ---------------------------------------------
//        var currentPoint = driver.findElement(By.id("active-pointer"));
//        System.out.println(currentPoint.getAttribute("outerHTML"));
//
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//
//        js.executeScript("""
//                arguments[0]
//                .setAttribute('style', arguments[0].getAttribute('style')
//                .replace('display: none;', 'top: 384px; left: 960px; display: none;'));
//                """, currentPoint);
//
//        System.out.println(currentPoint.getAttribute("outerHTML"));
//        Actions actions = new Actions(driver);
//
//        actions
//                .moveToElement(currentPoint)
//                .pause(Duration.ofMillis(500))
//                .click()
//                .perform();
//
//        System.out.println(currentPoint.getAttribute("outerHTML"));
// ---------------------------------------------


//        new Actions(driver)
//                .click(currentPoint)  // явно указываем элемент для клика
//                .perform();
//        // Создаем JavascriptExecutor
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//
//// Или более безопасный вариант с get/setAttribute
//        js.executeScript("""
//                arguments[0]
//                .setAttribute('style', arguments[0].getAttribute('style')
//                .replace(/top:\\s*\\d+px/, 'top: 384px')
//                .replace(/left:\\s*\\d+px/, 'left: 960px'))
//
//                """, currentPoint);
//        AppUtils.sleep();
//
//        currentPoint.click();


//        // Нажимаем на клетку напротив двери
//        Logger.getInstance().log("Заходим в ратушу...");
//        driver.findElement(By.xpath(location.getCityCenterDoor())).click();
//        AppUtils.sleep();
//
//        // Нажимаем на появившуюся зеленую стрелку
//        driver.findElement(By.className("exitbutton")).click();
//
//        AppUtils.sleep();
//
//        // Нажимаем на стойку с NPC
//        Logger.getInstance().log("Лечим покемонов...");
//        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[308]")).click();
//        AppUtils.sleep();
//
//        // Вызываем диалог с NPC
//        driver.findElement(By.className("VoiceNpc")).click();
//        AppUtils.sleep();
//
//        // Получаем окно диалога
//        var dialogWindow = driver.findElement(By.className("modal_npc_voice"));
//        // Получаем элемент с ответами
//        var answers = dialogWindow.findElement(By.className("Answer"));
//        // Выбираем первый ответ из списка (Вылечить покемонов)
//        answers.findElements(By.tagName("div")).get(0).click();
//
//        AppUtils.sleep();
//
//        // Идем обратно к двери
//        Logger.getInstance().log("Выходим из ратуши...");
//        driver.findElement(By.xpath("//*[@id=\"coord-layer\"]/div[316]")).click();
//
//        AppUtils.sleep();
//
//        // Нажимаем на пояявившуюся зеленую стрелку
//        driver.findElement(By.className("exitbutton")).click();
//
//        AppUtils.sleep();
//
//        // Возвращаемся на стартовую позицию
//        Logger.getInstance().log("Возврат на стартовую позицию...");
//        driver.findElement(By.xpath(location.getStartPosition())).click();
//
//        AppUtils.sleep();
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

    public void changeLocation(Location location) {
        Logger.getInstance().log("Смена локации. Открываем карту...");
        driver.findElement(By.className("mapBl")).click();

        AppUtils.sleep();

        Logger.getInstance().log("Выбираем " + location.getLocationName() + "...");

//        WebElement element = driver.findElement(By.className(location.getLocationClassName()));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
//        AppUtils.sleep();
//        element.click();

        driver.findElement(By.className(location.getLocationClassName())).click();

        AppUtils.sleep();

        WebElement teleportationButton = AppUtils.findByClass(driver, "btnFlyGo");
        AppUtils.sleep(1000L);

        if (teleportationButton != null) {
            Logger.getInstance().log("Телепортируемся...");
            teleportationButton.click();

            AppUtils.sleep();
            Logger.getInstance().log("Персонаж успешно перемещен в " + location.getLocationName());
        } else {
            Logger.getInstance().log("Персонаж в " + location.getLocationName() + ". Телепортация не требуется");
            driver.findElement(By.xpath("//*[@id=\"drgModel\"]/span/i")).click();
            AppUtils.sleep();
        }

    }

}
