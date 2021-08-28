package com.example.spacehero;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {
    private Pane root;
    Stage primaryStage;
    private int lev = 1;

    private int score = 0;

    Sprite player = new Sprite(300,750,40,40,"player", Color.TRANSPARENT,lev);
    Sprite endLine = new Sprite(0, 5*-200, 600, 3, "end", Color.TRANSPARENT,lev);


    Sprite plLife = new Sprite(10, 20, 300, 10, "life", Color.ORANGE,lev);

    Label label;
    AnimationTimer timer;


    public Game() throws FileNotFoundException {
    }

    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n->(Sprite)n).collect(Collectors.toList());
    }

    private Parent createContent() throws FileNotFoundException {
        root = new Pane();
        root.getChildren().add(player);
        root.getChildren().add(endLine);
        root.getChildren().add(plLife);

        root.setPrefSize(600,800);

        timer = new AnimationTimer(){

            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
        newLevel(lev+2);
        return root;
    }

    private void newLevel(int maxEnemyInaLine) throws FileNotFoundException {

        Random random = new Random();
        //int rang = random.nextInt(550);
        //if(rang < 450) rang = 450;

        for(int j = 0 ; j < 20 ; j++) {
            int x = random.nextInt(maxEnemyInaLine);//
            if(x<2) x = (int)(maxEnemyInaLine/2);
            for (int i = 0; i < x; i++) {
                Sprite s = new Sprite(90 + i * 500/x, j*-200, 30, 30, "enemy", Color.RED, lev);
                root.getChildren().add(s);
            }
        }
    }


    private void update() {
        sprites().forEach(s -> {
            switch(s.type){
                case "playerbullet" :
                    s.moveUp();
                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if(s.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                            enemy.dead = true;
                            score += 10;
                            s.dead = true;
                        }
                    });
                    break;
                case "enemybullet" :
                    s.setTranslateY(s.getTranslateY()+5);
                    sprites().stream().filter(e -> e.type.equals("player")).forEach(player -> {
                        if(s.getBoundsInParent().intersects(player.getBoundsInParent())){
                            player.dead = true;
                            s.dead = true;
                        }
                    });
                    break;
                case "enemy" :
                case "end" :
                    s.moveDown();
                    sprites().stream().filter(e -> e.type.equals("player")).forEach(player -> {
                        if(s.getBoundsInParent().intersects(player.getBoundsInParent())){
                            if(player.life==1) player.dead = true;
                            player.life--;
                            plLife.setWidth(plLife.getWidth()-100);
                            s.dead = true;
                        }
                    });
                    break;
            }
        });

        root.getChildren().removeIf(n->{
            Sprite s = (Sprite) n;
            if(s.type.equals("player") && s.dead && !endLine.dead){
                root.getChildren().clear();
                Stage stage = new Stage();

                Text txt = new Text("Score: "+score+". Retry?");
                Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
                txt.setFont(font);

                Button button = new Button("CONTINUE");

                button.setOnAction(e -> {
                    player.dead = false;
                    endLine.dead = false;
                    primaryStage.close();
                    timer.stop();

                    try {
                        //lev = 1;
                        player = new Sprite(300,750,40,40,"player", Color.TRANSPARENT,lev);
                        endLine = new Sprite(0, 5*-200, 600, 3, "end", Color.TRANSPARENT,lev);
                        plLife = new Sprite(10, 20, 300, 10, "life", Color.ORANGE,lev);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    root.getChildren().clear();
                    try {
                        //Scene scene = new Scene(createContent());
                        gameScene();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    stage.close();
                });
                //Creating a vbox to hold the button and the label
                HBox pane = new HBox(15);
                //Setting the space between the nodes of a HBox pane
                pane.setPadding(new Insets(50, 150, 50, 60));
                pane.getChildren().addAll(txt, button);

                //Creating a scene object
                Scene scene = new Scene(new Group(pane),500, 300);
                stage.setTitle("Game Over");
                stage.setScene(scene);
                stage.show();
            }
            if(s.type.equals("end")&&s.dead) {
                root.getChildren().clear();
                Stage stage = new Stage();

                if (lev == 5) {
                    Text txt = new Text("Score: "+score+".Congratulations!!Return to main menu.");
                    Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
                    txt.setFont(font);

                    Button button = new Button("Main Menu");

                    button.setOnAction(e -> {
                        primaryStage.close();
                        stage.close();
                        Menu menu = new Menu();
                        try {
                            menu.initMenu();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    //Creating a vbox to hold the button and the label
                    HBox pane = new HBox(15);
                    //Setting the space between the nodes of a HBox pane
                    pane.setPadding(new Insets(50, 150, 50, 60));
                    pane.getChildren().addAll(txt, button);

                    //Creating a scene object
                    Scene scene = new Scene(new Group(pane), 500, 300);
                    stage.setTitle("Game Finished");
                    stage.setScene(scene);
                } else {
                    lev++;
                    Text txt = new Text("Score: "+score+". Level Up. Continue to level " + lev + '?');
                    Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
                    txt.setFont(font);

                    Button button = new Button("NEW LEVEL");

                    button.setOnAction(e -> {
                        primaryStage.close();
                        player.dead = false;
                        endLine.dead = false;
                        timer.stop();
                        try {
                            player = new Sprite(300, 750, 40, 40, "player", Color.TRANSPARENT, lev);
                            endLine = new Sprite(0, 5 * -200, 600, 3, "end", Color.TRANSPARENT, lev);
                            plLife = new Sprite(10, 20, 300, 10, "life", Color.ORANGE,lev);
                            stage.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
//                    root.getChildren().clear();
//                    try {
//                        Scene scene = new Scene(createContent());
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    stage.close();
                        try {
                            gameScene();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    });
                    //Creating a vbox to hold the button and the label
                    HBox pane = new HBox(15);
                    //Setting the space between the nodes of a HBox pane
                    pane.setPadding(new Insets(50, 150, 50, 60));
                    pane.getChildren().addAll(txt, button);

                    //Creating a scene object
                    Scene scene = new Scene(new Group(pane), 500, 300);
                    stage.setTitle("Game Over");
                    stage.setScene(scene);
                }
                stage.show();
            }
            return s.dead;
        });
    }


    public void gameScene() throws FileNotFoundException {

        primaryStage = new Stage();

        Scene scene = new Scene(createContent());

        FileInputStream url = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/cosmos.jpg");

        Image bgimage = new Image(url);

        //ImagePattern myBI = new ImagePattern(bgimage);
        BackgroundImage backgroundimage = new BackgroundImage(bgimage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        // create Background
        Background background = new Background(backgroundimage);

        root.setBackground(background);

        scene.setOnKeyPressed(e -> {
            switch(e.getCode()){
                case A : player.moveLeft(); break;
                case D : player.moveRight(); break;
                case X: player.moveDown(); break;
                case W : player.moveUp(); break;

                case SPACE :
                    Sprite bullet = null;
                    try {
                        bullet = player.shoot(player);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    root.getChildren().add(bullet);
                    break;
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}