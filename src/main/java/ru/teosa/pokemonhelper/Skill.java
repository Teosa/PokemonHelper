package ru.teosa.pokemonhelper;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

@Data
public class Skill {

    private WebElement skillButton;

    private int maxPoints;

    private int currentPoints;

    private String name;

    public Skill(WebElement webElement) {
        this.skillButton = webElement;
        System.out.println("получение наименования скила");
        this.name = webElement.findElement(By.className("nameMove")).getText();
        System.out.println("получение количества пойнтов скила");
        String skillPointValue = webElement.findElement(By.className("ppMove")).getText(); // 7/15

        List<String> skillPointValues = Arrays.stream(skillPointValue.split("/")).toList();

        this.currentPoints = Integer.parseInt(skillPointValues.get(0));
        this.maxPoints = Integer.parseInt(skillPointValues.get(1));
    }
}
