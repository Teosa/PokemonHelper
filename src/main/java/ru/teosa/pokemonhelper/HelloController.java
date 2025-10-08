package ru.teosa.pokemonhelper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");

        new Thread(() -> {

            WebDriver driver = new ChromeDriver();
            try {
                driver.get("https://pokeroute.ru/world");
                driver.manage().window().maximize();
                Thread.sleep(3000); // Позволить увидеть открытие сайта

                var loginButton = driver.findElement(By.xpath("/html/body/header/ul/li[3]"));

                if (loginButton != null && loginButton.isDisplayed()) {
                    loginButton.click();
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                driver.quit();
            }
        }).start();
    }


}