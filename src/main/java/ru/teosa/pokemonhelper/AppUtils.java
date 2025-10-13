package ru.teosa.pokemonhelper;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppUtils {

    public static WebElement findByCssSelector(WebElement element, String cssSelector) {
        try {
            return element.findElement(By.cssSelector(cssSelector));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public static WebElement findByXPath(WebElement element, String xpath) {
        try {
            return element.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public static WebElement findByXPath(WebDriver driver, String xpath) {
        try {
            return driver.findElement(By.xpath(xpath));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentDateTimeFormated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(LocalDateTime.now());
    }
}
