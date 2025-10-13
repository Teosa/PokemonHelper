package ru.teosa.pokemonhelper;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@AllArgsConstructor
public class BattleService {

    private WebDriver driver;

    public boolean battle() {
        var enemyInfoPanel = driver.findElement(By.xpath("//*[@id=\"battleMap\"]/div/div[3]/div[1]/div[5]/div[3]"));
        EnemyInfoPanel enemyInfo = new EnemyInfoPanel(enemyInfoPanel);

        Logger.getInstance().log("Начало боя с " + enemyInfo.getName());

        var playerInfoPanel = driver.findElement(By.xpath("//*[@id=\"battleMap\"]/div/div[3]/div[1]/div[3]/div[3]"));
        PlayerInfoPanel playerInfo = new PlayerInfoPanel(playerInfoPanel, findBattlePanel());

        int attemptsQty = 0;

        while (enemyInfo.getCurrentHp() > 0) {
            AppUtils.sleep(2000L);

            if (playerInfo.getCurrentHp() <= 50) {
                Logger.getInstance().log("Низкий уровень HP: " + playerInfo.getCurrentHp() + ". Выходим из битвы...");
                driver.findElement(By.className("run_ui_b")).click();
                AppUtils.sleep();

                driver.switchTo().alert().accept();
                AppUtils.sleep();

                driver.findElement(By.className("LeaveButton")).click();
                AppUtils.sleep();

                return false;
            }

            try {
                attemptsQty++;
                var skill = playerInfo.getAvalibleSkill();
                skill.click(driver);

                AppUtils.sleep(2000L);

                enemyInfo.refreshHp();

                Logger.getInstance().log("Применен " + skill.getName() + ". HP противника: " + enemyInfo.getCurrentHp());
            }
            catch (ElementClickInterceptedException ecle) {
                System.out.println("------> ecle");
            } catch (Exception e) {
                if (attemptsQty > 3) {
                    throw e;
                }
            }
        }

        Logger.getInstance().log(enemyInfo.getName() + " побежден");

        driver.findElement(By.className("LeaveButton")).click();

        return playerInfo.countAvalibleSkills() > 2;
    }

    private WebElement findBattlePanel() {
        return driver.findElement(By.className("move_battle"));
    }
}
