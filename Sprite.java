package com.example.spacehero;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Sprite extends Rectangle {
    boolean dead = false;
    final String type;
    Image img;
    int level;
    int life = 1;
    FileInputStream hero = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/rocket.png");
    FileInputStream villain;

    public Sprite(double x, double y, double w, double h, String type, Color color, int lev) throws FileNotFoundException {
        super(w, h,color);
        this.type = type;
        this.level = lev;
        setTranslateX(x);
        setTranslateY(y);
        if(type.equals("player")){
            img = new Image(hero);
            this.setFill(new ImagePattern(img));
            this.life = 3;
        }
        if(lev==1){
            villain = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/alien.png");
        }
        if(lev==2){
            villain = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/alien2.png");
        }
        if(lev==3){
            villain = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/alien3.png");
        }
        if(lev==4){
            villain = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/alien4.png");
        }
        if(lev==5){
            villain = new FileInputStream("d:\\Users\\Tahmid\\Documents\\sample\\icons/alien3.png");
        }
        if(type.equals("enemy")){
            img = new Image(villain);
            this.setFill(new ImagePattern(img));
        }
    }

    void moveLeft() {
        setTranslateX(getTranslateX()-20);
    }
    void moveRight(){
        setTranslateX(getTranslateX()+20);
    }
    void moveUp(){
        setTranslateY(getTranslateY()-3);
    }
    void moveDown(){
        setTranslateY(getTranslateY()+2);
    }
    public Sprite shoot(Sprite who) throws FileNotFoundException {
        return new Sprite(who.getTranslateX()+18, who.getTranslateY(),5,10,who.type+"bullet", Color.RED,this.level);
    }
}
