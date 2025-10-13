"C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot\bin\jpackage.exe"  ^
 --input "D:\IdeaProjects\Heap\PokemonHelper\jpackage-in2" ^
 --name PokemonHelper  ^
 --main-jar PokemonHelper-1.0.jar  ^
 --main-class ru.teosa.pokemonhelper.HelloApplication  ^
 --java-options "--add-opens ru.teosa.pokemonhelper/ru.teosa.pokemonhelper=ALL-UNNAMED" ^
 --type app-image ^
 --module-path "D:\javafx-sdk-17.0.16\lib"  ^
 --add-modules javafx.controls,javafx.fxml ^
 --dest "D:\IdeaProjects\Heap\PokemonHelper\pokehelp"
 
pause

