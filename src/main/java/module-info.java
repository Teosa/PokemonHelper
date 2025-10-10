module ru.teosa.pokemonhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires com.google.common;
    requires static lombok;

    opens ru.teosa.pokemonhelper to javafx.fxml;
    exports ru.teosa.pokemonhelper;
}