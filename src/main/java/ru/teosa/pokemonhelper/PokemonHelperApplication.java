package ru.teosa.pokemonhelper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.teosa.pokemonhelper.controller.StartButtonController;
import ru.teosa.pokemonhelper.utils.AppUtils;

import java.io.IOException;

public class PokemonHelperApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PokemonHelperApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();

        StartButtonController controller = fxmlLoader.getController();
        controller.setDriver(new ChromeDriver());

        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Pokemon Helper");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            controller.getDriver().quit();
            stage.hide();
            AppUtils.sleep();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}