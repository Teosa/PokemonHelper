package ru.teosa.pokemonhelper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HelloController {

    private final LoginService loginService;
    private final MovementService movementService;

    private final WebDriver driver;

    public HelloController() {
        super();
        this.driver = new ChromeDriver();
        this.loginService = new LoginService(driver);
        this.movementService = new MovementService(driver);
    }

    @FXML
    private Label welcomeText;

    @FXML
    private TextField enemyCountField;

    @FXML
    private TextArea myTextArea;

//    @FXML
//    protected void onTestClick() {
//
//    }

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome");
        movementService.setEnemyCountField(enemyCountField);
        Logger.getInstance(myTextArea).log("Запуск бота...");

        new Thread(() -> {
            try {
                driver.get("https://pokeroute.ru/world");
                driver.manage().window().maximize();
                Thread.sleep(3000);

                // Логинимся
                loginService.login();

                movementService.moveToFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}