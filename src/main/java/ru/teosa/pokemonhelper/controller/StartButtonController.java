package ru.teosa.pokemonhelper.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.teosa.pokemonhelper.configuration.Properties;
import ru.teosa.pokemonhelper.service.LoginService;
import ru.teosa.pokemonhelper.service.MovementService;
import ru.teosa.pokemonhelper.utils.AppUtils;
import ru.teosa.pokemonhelper.utils.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class StartButtonController implements Initializable {

    @Setter
    @Getter
    private  WebDriver driver;

    private LoginService loginService;

    private MovementService movementService;

    private boolean isBrowserStarted;

    private boolean isLoggedIn;

    public StartButtonController() {
        super();
    }

    @FXML
    private Label infoText;

    @FXML
    private TextField enemyCountField;

    @FXML
    private TextField enemyTargetQty;

    @FXML
    private TextArea myTextArea;

    @FXML
    private Label appVersion;

    @FXML
    private Button startButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loginService = new LoginService(driver);
        this.movementService = new MovementService(driver);

        appVersion.setText("Version " + AppUtils.getApplicationVersion());

        // Предзаполняем поле сразу после открытия формы
        enemyTargetQty.setText("1000");
    }

    @FXML
    protected void onStartButtonClick() {
        myTextArea.clear();
        Properties.getInstance(enemyTargetQty.getText());
        movementService.setEnemyCountField(enemyCountField);

        startButton.setDisable(true);
        enemyTargetQty.setDisable(true);

        infoText.setText("Бот запущен " + AppUtils.getCurrentDateTimeFormated());

        Logger.getInstance(myTextArea).log("Запуск бота...");

        Thread browserThread = new Thread(() -> {
            try {
                if (!isBrowserStarted) {
                    driver.get("https://pokeroute.ru/world");
                    driver.manage().window().maximize();
                    Thread.sleep(3000);

                    isBrowserStarted = true;
                }

                if (!isLoggedIn) {
                    loginService.login();
                    isLoggedIn = true;
                }

                while (!Properties.getInstance().isLimitOver(enemyCountField.getText())) {
                    myTextArea.clear();
                    movementService.moveToFields();
                    movementService.heal();
                }

//                infoText.setText("Бот завершил работу " + AppUtils.getCurrentDateTimeFormated());
            } catch (Exception e) {
                Logger.getInstance(myTextArea).log("Произошла ошибка. Бот остановлен");
                infoText.setText("Произошла ошибка. Бот остановлен. Для возобновления работы нажмите кнопку Запуск");
                e.printStackTrace();
                Logger.getInstance(myTextArea).log(getStackTraceAsString(e));
            } finally {
                startButton.setDisable(false);
                enemyTargetQty.setDisable(false);
            }
        });

        browserThread.setDaemon(true);
        browserThread.start();
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }


}