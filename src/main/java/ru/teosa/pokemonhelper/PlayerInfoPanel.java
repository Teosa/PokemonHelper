package ru.teosa.pokemonhelper;

import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlayerInfoPanel {

    private int maxHp;

    private int currentHp;

    private List<Skill> skills;

    public PlayerInfoPanel(WebElement infoPanel, WebElement battlePanel) {
        System.out.println("получение hpBarValue");
        String hpBarValue = infoPanel.findElement(By.className("hp_pok")).getText(); // 351 / 355
        List<String> hpBarValues = Arrays.stream(hpBarValue.split(" / ")).toList();

        this.currentHp = Integer.parseInt(hpBarValues.get(0));
        this.maxHp = Integer.parseInt(hpBarValues.get(1));
        System.out.println("получение списка дивов со скилами");
        List<WebElement> skills = battlePanel.findElements(By.xpath("div"));

        this.skills = skills.stream()
                .map(Skill::new)
                .collect(Collectors.toList());
    }

    public Skill getAvalibleSkill() {
        return skills.stream()
                .filter(skill -> skill.getCurrentPoints() > 0)
                .findFirst()
                .orElse(null);
    }

    public int countAvalibleSkills() {
        return skills.stream()
                .mapToInt(Skill::getCurrentPoints)
                .sum();
    }


}
