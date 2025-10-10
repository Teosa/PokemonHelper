package ru.teosa.pokemonhelper;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@AllArgsConstructor
public class BattleService {

    private WebDriver driver;

    public boolean battle() {
        var enemyInfoPanel = driver.findElement(By.xpath("//*[@id=\"battleMap\"]/div/div[3]/div[1]/div[5]/div[3]"));
        EnemyInfoPanel enemyInfo = new EnemyInfoPanel(enemyInfoPanel);

        Logger.getInstance().log("Начало боя с " + enemyInfo.getName());

        var playerInfoPanel = driver.findElement(By.xpath("//*[@id=\"battleMap\"]/div/div[3]/div[1]/div[3]/div[3]"));
        var battlePanel = driver.findElement(By.xpath("//*[@id=\"battleMap\"]/div/div[3]/div[2]"));
        PlayerInfoPanel playerInfo = new PlayerInfoPanel(playerInfoPanel, battlePanel);


        while (enemyInfo.getCurrentHp() > 0) {
            System.out.println("enemy HP: " + enemyInfo.getCurrentHp());
            AppUtils.sleep(2000L);
            playerInfo.getAvalibleSkill().getSkillButton().click();
            AppUtils.sleep(2000L);
            enemyInfo.refreshHp();
        }

        Logger.getInstance().log(enemyInfo.getName() + " побежден");

        driver.findElement(By.className("LeaveButton")).click();

        return playerInfo.countAvalibleSkills() > 2;
    }
}
