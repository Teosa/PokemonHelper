package ru.teosa.pokemonhelper.service;


import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.teosa.pokemonhelper.utils.AppUtils;
import ru.teosa.pokemonhelper.utils.Logger;

@AllArgsConstructor
public class LoginService {

    private WebDriver driver;

    public void login() {

        Logger.getInstance().log("Поиск кнопки логина...");
        var loginButton = driver.findElement(By.xpath("/html/body/header/ul/li[3]"));

        if (loginButton != null && loginButton.isDisplayed()) {
            loginButton.click();
        }

        Logger.getInstance().log("Поиск панели логина...");
        var loginForm = driver.findElement(By.cssSelector("body > div.loginpanel"));

        AppUtils.sleep();

        if (loginForm.isDisplayed()) {
            Logger.getInstance().log("Поиск кнопки 'В игру'...");
            var intoGameButton = AppUtils.findByXPath(loginForm, "/html/body/div[2]/div[1]/center/a[1]");

            if (intoGameButton != null && intoGameButton.isDisplayed()) {
                Logger.getInstance().log("Кнопка найдена, логинимся");
                intoGameButton.click();
            } else {
                Logger.getInstance().log("Кнопка не найдена, заполняем форму...");
                var loginField = loginForm.findElement(By.cssSelector("#uLogin"));
                var passwordField = loginForm.findElement(By.cssSelector("#uPass"));
                var sendLoginFormButton = loginForm.findElement(By.cssSelector("#autorizeForm > button"));

                loginField.sendKeys("");
                passwordField.sendKeys("");

                Logger.getInstance().log("Логинимся");
                sendLoginFormButton.click();

                AppUtils.sleep();

                Logger.getInstance().log("Нажимаем 'В игру'...");
                loginForm.findElement(By.xpath("/html/body/div[2]/div[1]/center/a[1]")).click();

                AppUtils.sleep();

                Logger.getInstance().log("Нажимаем 'Войти в мир'");
                driver.findElement(By.xpath("/html/body/div[1]/div/div[2]")).click();


                Logger.getInstance().log("--------------------");
            }
        }
    }

}
