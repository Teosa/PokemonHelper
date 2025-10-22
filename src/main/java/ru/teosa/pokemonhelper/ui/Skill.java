package ru.teosa.pokemonhelper.ui;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.teosa.pokemonhelper.utils.AppUtils;

import java.util.Arrays;
import java.util.List;

@Data
public class Skill {

    private String webElementClassName;

    private int maxPoints;

    private int currentPoints;

    private String name;

    public Skill(WebElement webElement) {
        this.webElementClassName = webElement.getAttribute("class").split(" ")[1];
        this.name = webElement.findElement(By.className("nameMove")).getText();

        String skillPointValue = webElement.findElement(By.className("ppMove")).getText(); // 7/15

        List<String> skillPointValues = Arrays.stream(skillPointValue.split("/")).toList();

        this.currentPoints = Integer.parseInt(skillPointValues.get(0));
        this.maxPoints = Integer.parseInt(skillPointValues.get(1));
    }

    public void click(WebDriver webElement) {
        try {
            webElement.findElement(By.className(webElementClassName)).click();
        } catch (Exception e) {
            AppUtils.sleep(200L);
            webElement.findElement(By.className(webElementClassName)).click();
        }
    }
}
