package ru.teosa.pokemonhelper.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import ru.teosa.pokemonhelper.Location;
import ru.teosa.pokemonhelper.configuration.Properties;
import ru.teosa.pokemonhelper.service.LoginService;
import ru.teosa.pokemonhelper.service.MovementService;
import ru.teosa.pokemonhelper.utils.AppUtils;
import ru.teosa.pokemonhelper.utils.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class StartButtonController {

    @Setter
    @Getter
    private WebDriver driver;

    private LoginService loginService;

    private MovementService movementService;

    private boolean isBrowserStarted;

    private boolean isLoggedIn;

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

    @FXML
    private ComboBox<String> farmLocationCombo;

    public void init() {
        this.loginService = new LoginService(driver);
        this.movementService = new MovementService(driver);

        appVersion.setText("Version " + AppUtils.getApplicationVersion());

        // Предзаполняем поле сразу после открытия формы
        enemyTargetQty.setText("1000");

        fillFarmLocationCombo();
        farmLocationCombo.getSelectionModel().selectFirst();
    }


    @FXML
    protected void onStartButtonClick() {
        myTextArea.clear();
        Properties.getInstance(enemyTargetQty.getText());
        movementService.setEnemyCountField(enemyCountField);

        startButton.setDisable(true);
        enemyTargetQty.setDisable(true);
        farmLocationCombo.setDisable(true);

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

                Location selectedFarmLocation = Location.getByName(farmLocationCombo.getValue());

                while (!Properties.getInstance().isLimitOver(enemyCountField.getText())) {
                    movementService.changeLocation(selectedFarmLocation.getParent());
                    movementService.heal(selectedFarmLocation.getParent());
                    myTextArea.clear();
                    movementService.changeLocation(selectedFarmLocation);
                    movementService.work();
                }
            } catch (Exception e) {
                Logger.getInstance(myTextArea).log("Произошла ошибка. Бот остановлен");
                infoText.setText("Произошла ошибка. Бот остановлен. Для возобновления работы нажмите кнопку Запуск");
                e.printStackTrace();
                Logger.getInstance(myTextArea).log(getStackTraceAsString(e));
            } finally {
                startButton.setDisable(false);
                enemyTargetQty.setDisable(false);
                farmLocationCombo.setDisable(false);
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

    private void fillFarmLocationCombo() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        Arrays.stream(Location.values())
                                .filter(location -> location.getParent() != null)
                                .map(Location::getLocationName)
                                .toList()
                );

        farmLocationCombo.getItems().addAll(options);
    }


}