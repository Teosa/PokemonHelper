package ru.teosa.pokemonhelper.ui;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

@Data
public class EnemyInfoPanel {

    private WebElement element;

    private int currentHp;

    private String name;

    public EnemyInfoPanel(WebElement element) {
        this.element = element;
        this.currentHp = getCurrentHpFromWebElement();
        this.name = element.findElement(By.className("b-pok-name")).getText();
    }

    public void refreshHp() {
        this.currentHp = getCurrentHpFromWebElement();
    }

    private int getCurrentHpFromWebElement() {
        String hpBarValue = this.element.findElement(By.className("hp_pok")).getText(); // 351 / 355
        List<String> hpBarValues = Arrays.stream(hpBarValue.split(" / ")).toList();

        return Integer.parseInt(hpBarValues.get(0));
    }

}
