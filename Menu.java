package com.example.spacehero;



import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

import static javafx.application.Platform.exit;

public class Menu {
    Stage primaryStage;
    public void initMenu() throws IOException {


        AnchorPane root = new AnchorPane();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(100, 50, 50, 100));
        vbox.setSpacing(10);

        Button newGame = new Button("New Game");
        Button exit = new Button("Exit");

        newGame.setOnAction(e -> {
            try {
                gameOn();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        exit.setOnAction(e ->{
            exit();
        });

        vbox.getChildren().addAll(newGame,exit);

        root.getChildren().add(vbox);
        primaryStage = new Stage();
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    void gameOn() throws FileNotFoundException {
        Game game;
        game = new Game();
        game.gameScene();
    }
}