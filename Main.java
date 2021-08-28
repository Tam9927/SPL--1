package com.example.spacehero;




import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Menu menu = new Menu();
        menu.initMenu();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
