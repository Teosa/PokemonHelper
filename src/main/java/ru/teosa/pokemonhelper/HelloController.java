package ru.teosa.pokemonhelper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private  WebDriver driver;

    private  LoginService loginService;

    private  MovementService movementService;

    private boolean isBrowserStarted;

    private boolean isLoggedIn;

    public HelloController() {
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
    private Button startButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.driver = new ChromeDriver();
        this.loginService = new LoginService(driver);
        this.movementService = new MovementService(driver);

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

        new Thread(() -> {
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
        }).start();
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }


}