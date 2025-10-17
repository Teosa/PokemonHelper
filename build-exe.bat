cd jpackage-in2
del /Q *.*
cd ..\target

copy pokemon-helper-*.jar ..\jpackage-in2\

cd ..

RMDIR /S /Q .\pokehelp\

"C:\Program Files\Eclipse Adoptium\jdk-17.0.16.8-hotspot\bin\jpackage.exe"  ^
 --input "D:\IdeaProjects\Heap\PokemonHelper\jpackage-in2" ^
 --name PokemonHelper  ^
 --main-jar pokemon-helper-1.2.jar  ^
 --main-class ru.teosa.pokemonhelper.PokemonHelperApplication  ^
 --java-options "--module-path D:\javafx-sdk-17.0.16\lib" ^
 --java-options "--add-modules javafx.controls,javafx.fxml" ^
 --type app-image ^
--dest "D:\IdeaProjects\Heap\PokemonHelper\pokehelp"

pause

